/*
 * Copyright (c) 2009-2011 NVIDIA Corporation.  All Rights Reserved.
 *
 * NVIDIA Corporation and its licensors retain all intellectual property and
 * proprietary rights in and to this software and related documentation.  Any
 * use, reproduction, disclosure or distribution of this software and related
 * documentation without an express license agreement from NVIDIA Corporation
 * is strictly prohibited.
 */

#define GTK_OUTPUT

#ifndef _GNU_SOURCE
    #define _GNU_SOURCE
#endif
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

#include <string.h>
#include <dirent.h>
#include <fnmatch.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <signal.h>
#include <fcntl.h>

#ifndef GTK_OUTPUT
#if !NV_IS_LDK
    #include <utils/Log.h>
    #undef LOG_TAG
    #define LOG_TAG "TegraStats"
#else
    #include <string.h>
    #define LOGE(...)        \
    do {                     \
        printf(__VA_ARGS__); \
        printf("\n");        \
    } while (0)

    #define LOGI(...)        \
    do {                     \
        printf(__VA_ARGS__); \
        printf("\n");        \
    } while (0)
#endif

#define PRINTE(toLog, logFile,  ...)    \
do{                                     \
    if (toLog && (logFile != NULL))     \
    {                                   \
        fprintf(logFile, __VA_ARGS__);  \
        fprintf(logFile, "\n");         \
    }                                   \
    else                                \
    {                                   \
        LOGE(__VA_ARGS__);              \
    }                                   \
}while(0)
#endif
#define NVMAP_BASE_PATH "/sys/devices/platform/tegra-nvmap/misc/nvmap/"
#define CARVEOUT(x) NVMAP_BASE_PATH "heap-generic-0/" # x
#define IRAM(x)     NVMAP_BASE_PATH "heap-iram/" # x

#define EDP_LIMIT_DEBUGFS_PATH "/sys/kernel/debug/edp_limit"
#define DVFS_CLOCKS_BASE_PATH "/sys/kernel/debug/clock/"
#define EMCCLK DVFS_CLOCKS_BASE_PATH "emc/rate"
#define AVPCLK DVFS_CLOCKS_BASE_PATH "avp.sclk/rate"
#define VDECLK DVFS_CLOCKS_BASE_PATH "vde/rate"

#define READ_VALUE(bToLog, logFile, path, pvalue) {         \
    f = fopen(path, "r");                                   \
    if (f) {                                                \
        (void) fscanf(f, "%d", pvalue);                     \
        fclose(f);                                          \
    } else {                                                \
        PRINTE(bToLog, logFile, "Failed to open %s", path); \
    }                                                       \
}

#define NUM_SLOTS           11
#define PAGE_SIZE         4096
#define FREQUENCY_CONVERT 1000
#define AP20_CHIPID         20
#define T30_CHIPID          30
#define ALLOC_BUFFER_SIZE 1024

/* Prototypes. */

#ifndef GTK_OUTPUT
int main(int argc, char *argv[]);
#endif

static void logFlush(void);
static int B2MB(int bytes);
static int kB2MB(int kiloBytes);
static int B2kB(int bytes);
static int SmartB2Str(char* str, size_t size, int bytes);
#ifndef GTK_OUTPUT
static void setFreq(int setMax);
static long processdir(const struct dirent *dir);
static void signal_handler(int signal);
static int filter(const struct dirent *dir);
#endif
static int getChipId(void);

/* Store clk values to restore later */
// Assuming clk frequencies are same for both CPUs
unsigned int cpuclk[2];

FILE *f        = NULL;
FILE *logFile  = NULL;

/* Functions. */

static void logFlush(void)
{
#if NV_IS_LDK
    // need to fflush on LDK to make output redirectable
    fflush(stdout);
#endif
    if ((logFile != NULL) && (fileno(logFile) != -1))
    {
        fflush(logFile);
    }
}

static int B2MB(int bytes)
{
    bytes += (1<<19)-1;       // rounding
    return bytes >> 20;
}

static int kB2MB(int kiloBytes)
{
    kiloBytes += (1<<9)-1;    // rounding
    return kiloBytes >> 10;
}

static int B2kB(int bytes)
{
    bytes += (1<<9)-1;        // rounding
    return bytes >> 10;
}

static int SmartB2Str(char* str, size_t size, int bytes)
{
    if (bytes < 1024)
    {
        return snprintf(str, size, "%dB", bytes);
    }
    else if (bytes < 1024*1024)
    {
        return snprintf(str, size, "%dkB", B2kB(bytes));
    }
    else
    {
        return snprintf(str, size, "%dMB", B2MB(bytes));
    }
}
#ifndef GTK_OUTPUT

static void setFreq(int setMax)
{
    FILE* f;
    LOGI("setFreq %d", setMax);

    if (!cpuclk[0]) {
        f = fopen("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies", "r");
        if (f)
        {
            fscanf(f, "%u", &cpuclk[0]);
            while(fscanf(f, "%u", &cpuclk[1]) != EOF);

            LOGI("cpuclk: minfreq = %u maxfreq = %u\n", cpuclk[0], cpuclk[1]);
            fclose(f);
        }
        else
        {
            LOGE("Error opening file scaling_available_frequencies");
        }
    }
    if (setMax)
    {
        // set CPU frequency to highest value
        f = fopen("/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq", "w");
        if (f)
        {
            fprintf(f, "%u", cpuclk[1]);
            fclose(f);
        }
        else
        {
            LOGE("Error opening file scaling_min_freq\n");
        }
    }
    else
    {
        // set default
        f = fopen("/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq", "w");
        if (f)
        {
            fprintf(f, "%u", cpuclk[0]);
            fclose(f);
        }
        else
        {
            LOGE("Error opening file scaling_min_freq\n");
        }
    }
}

static long processdir(const struct dirent *dir)
{
     char path[256];
     char linkinfo[256];

     memset(path, 0, sizeof path);
     memset(linkinfo, 0, sizeof linkinfo);

     strcpy(path, "/proc/");
     strcat(path, dir->d_name);
     strcat(path, "/exe");
     readlink(path, linkinfo, sizeof linkinfo);
     if (strstr(linkinfo, "tegrastats") != NULL)
     {
        return strtol(dir->d_name, (char **) NULL, 10);
     }
     return 0;
}

static void signal_handler(int signal)
{
    if ((f != NULL) && (fileno(f) != -1))
    {
        fclose(f);
    }
    logFlush();
    if ((logFile != NULL) && (fileno(logFile) != -1))
    {
        fclose(logFile);
    }
    exit(0);
}

static int filter(const struct dirent *dir)
{
    return !fnmatch("[1-9]*", dir->d_name, 0);
}
#endif

static int getChipId(void)
{
    char *contents = NULL;
    char *tegraid  = NULL;
    int count  = 0;
    int chipid = 0;

    /* open file */
    int fd = open("/proc/cmdline", O_RDONLY);
    if (fd < 0)
    {
        printf("Couldn't open %s\n", "/proc/cmdline");
        goto failout;
    }

    /* allocate enough memory */
    contents = malloc(ALLOC_BUFFER_SIZE);
    if (!contents)
    {
        printf("Couldn't allocate mem %d bytes\n", ALLOC_BUFFER_SIZE);
        goto failout;
    }

    /* read the contents of the file */
    count = read(fd, contents, ALLOC_BUFFER_SIZE-1);
    if (count < 0)
    {
        printf("Couldn't read the file %s\n", "/proc/cmdline");
        goto failout;
    }

    /* add zero to make it a string */
    contents[count] = '\0';

    tegraid = strstr(contents, "tegraid=");
    if (tegraid)
    {
        tegraid += strlen("tegraid=");
        chipid = atoi(tegraid);
    }

failout:
    if (fd >= 0)
    {
        close(fd);
    }
    free(contents);
    return chipid;
}
#ifndef GTK_OUTPUT

int main (int argc, char *argv[])
{
    int i;
    unsigned int sleepMS = 1000;
    int isCpu0Active;
    int isCpu1Active;
    int cpuLoadPrev[4*10];
    int cpu0Load = 0;
    int cpu1Load = 0;
    int cpu2Load = 0;
    int cpu3Load = 0;
    int isCpu2Active;
    int isCpu3Active;
    int debug  = 0;
    int bStart = 0;
    int bStop  = 0;
    int bToLog = 0;
    int chipId = 0;
    int pid    = 0;
    char logName[256] = {0};

    memset(cpuLoadPrev, 0, 4*10*sizeof(int));

    for (i = 1; i < argc; i++)
    {
        if (argv && argv[i])
        {
            LOGE("argv[%d] = %s\n", i, argv[i]);

            if (argv[i][0] == '-')
            {
                if (!strcmp(argv[i], "-max"))
                {
                    setFreq(1);
                    LOGI("Set all components to max frequency");
                    return 0;
                }
                else if (!strcmp(argv[i], "-default"))
                {
                    setFreq( 0);
                    return 0;
                }
                else if (!strcmp(argv[i], "-debug"))
                {
                    debug = 1;
                }
                else if (!strcmp(argv[i], "--start"))
                {
                    bStart = 1;
                }
                else if (!strcmp(argv[i], "--stop"))
                {
                    bStop = 1;
                }
                else if (!strcmp(argv[i], "--logfile"))
                {
                    if ((i+1) <argc)
                    {
                        strcpy(logName, argv[i+1]);
                        i++;
                        bToLog = 1;
                    }
                }
            }
            else
            {
                sscanf(argv[1], "%d", &sleepMS);
                if (sleepMS < 100)
                    sleepMS = 100;
            }
        }
    }

    if (bStop)
    {
        struct dirent **namelist;
        int n;

        n = scandir("/proc", &namelist, filter, 0);
        if (n < 0)
            perror("Not enough memory.");
        else
        {
            while (n--)
            {
                pid = processdir(namelist[n]);
                if ((pid != 0) && (getpid() != (pid_t) pid))
                {
                    kill((pid_t) pid, SIGTERM);
                }
                free(namelist[n]);
            }
            free(namelist);
        }

        return 0;
    }
    else if (bStart)
    {
        // run in background
        pid = fork();
        if (pid > 0)
        {
            // parent exit now..
            exit(0);
        }
        else if (pid == 0)
        {
            setpgrp();
            signal(SIGINT, signal_handler);
            signal(SIGTERM, signal_handler);
        }
        else
        {
            // print warning, but do not exit..
            LOGE("failed to fork a child process \n");
        }
    }

    if (strlen(logName))
    {
        if ((logFile = fopen(logName, "a")) == NULL)
        {
            LOGE("failed to open %s \n", logName);
            bToLog = 0;
        }
    }
#else

#define PRINTE(toLog, logFile,  ...)    \
do{                                     \
    (void) toLog; \
    (void)logFile; \
    sprintf(str, __VA_ARGS__);      \
                                        \
}while(0)

int GetStats(char * str)
{
  int isCpu0Active;
  int isCpu1Active;
  static int cpuLoadPrev[4*10];
  int cpu0Load = 0;
  int cpu1Load = 0;
  int cpu2Load = 0;
  int cpu3Load = 0;
  int isCpu2Active;
  int isCpu3Active;
  int bToLog = 0;
  int chipId = 0;

#endif
    chipId = getChipId();

#ifndef GTK_OUTPUT
    for (;;)
#endif
    	{
        int totalRAMkB = -1;
        int freeRAMkB = -1;
        int largestFreeRAMBlockB = -1;
        int numLargestRAMBlock = -1;
        int buffersRAMkB = -1;
        int cachedRAMkB = -1;
        int totalCarveoutB = -1;
        int freeCarveoutB = -1;
        int largestFreeCarveoutBlockB = -1;
        int totalGARTkB = -1;
        int freeGARTkB = -1;
        int largestFreeGARTBlockkB = -1;
        int totalIRAMB = -1;
        int freeIRAMB = -1;
        int largestFreeIRAMBlockB = -1;
        int currCpuFreq = -1;
        int emcClk = -1;
        int avpClk = -1;
        int vdeClk = -1;
        int no_cpus = 4;
        int edp_limit = -1;

        // RAM
        f = fopen("/proc/meminfo", "r");
        if (f)
        {
            (void) fscanf(f, "MemTotal: %d kB\n", &totalRAMkB);
            (void) fscanf(f, "MemFree: %d kB\n", &freeRAMkB);
            (void) fscanf(f, "Buffers: %d kB\n", &buffersRAMkB);
            (void) fscanf(f, "Cached: %d kB\n", &cachedRAMkB);

            fclose(f);
        }
        else
        {
            PRINTE(bToLog, logFile, "Failed to open /proc/meminfo");
        }

        f = fopen("/proc/buddyinfo", "r");
        if (f)
        {
            char line[256];
            int lineNum = 0;
            int slots[NUM_SLOTS];
            int i;

            //
            // Get the number of free blocks for each size.
            // Separation into nodes and zones is not kept.
            //
            while (fgets(line, sizeof(line), f))
            {
                int j = 0;
                int n;
                int tmpSlots[NUM_SLOTS];
                char* buf = line;

                (void) sscanf(buf, "Node %*d, zone %*s%n", &n);
                buf += n;

                while (sscanf(buf, "%d%n", &tmpSlots[j], &n) == 1)
                {
                    buf += n;
                    slots[j] = lineNum ? slots[j] + tmpSlots[j] : tmpSlots[j];
                    j++;
                }

                lineNum++;
            }

            fclose(f);

            // Extract info about the largest available blocks
            i = NUM_SLOTS - 1;
            while (slots[i] == 0 && i > 0)
                i--;
            numLargestRAMBlock = slots[i];
            largestFreeRAMBlockB = (1 << i) * PAGE_SIZE;
        }
        else
        {
            PRINTE(bToLog, logFile, "Failed to open /proc/buddyinfo");
        }

        // CPU 0/1 On/Off
        f = fopen("/sys/devices/system/cpu/cpu0/online", "r");
        if (f)
        {
            (void) fscanf(f, "%d", &isCpu0Active);
            fclose(f);
        }
        else
        {
            PRINTE(bToLog, logFile, "/sys/devices/system/cpu/cpu0/online");
        }

        f = fopen("/sys/devices/system/cpu/cpu1/online", "r");
        if (f)
        {
            (void) fscanf(f, "%d", &isCpu1Active);
            fclose(f);
        }
        else
        {
            PRINTE(bToLog, logFile, "/sys/devices/system/cpu/cpu1/online");
        }

        // CPU 2/3 On/Off
        f = fopen("/sys/devices/system/cpu/cpu2/online", "r");
        if (f)
        {
            (void) fscanf(f, "%d", &isCpu2Active);
            fclose(f);
        }
        else
        {
            no_cpus = 2;
        }

        if (no_cpus == 4)
        {
            f = fopen("/sys/devices/system/cpu/cpu3/online", "r");
            if (f)
            {
                (void) fscanf(f, "%d", &isCpu3Active);
                fclose(f);
            }
        }

        // EDP limit
        f = fopen(EDP_LIMIT_DEBUGFS_PATH, "r");
        if (f)
        {
            (void) fscanf(f, "%d", &edp_limit);
            fclose(f);
        }

        // CPU load
        f = fopen("/proc/stat", "r");
        if (f)
        {
            int c[40];
            int l[40];
            int i;

            //
            // from http://www.mjmwired.net/kernel/Documentation/filesystems/proc.txt
            // Various pieces of information about kernel activity are available
            // in the /proc/stat file. All of the numbers reported in this file
            // are aggregates since the system first booted. The very first
            // "cpu" line aggregates the numbers in all of the other "cpuN"
            // lines. These numbers identify the amount of time the CPU has spent
            // performing different kinds of work. Time units are in USER_HZ
            // (typically hundredths of a second). The meanings of the columns
            // are as follows, from left to right:
            //  - user: normal processes executing in user mode
            //  - nice: niced processes executing in user mode
            //  - system: processes executing in kernel mode
            //  - idle: twiddling thumbs
            //  - iowait: waiting for I/O to complete
            //  - irq: servicing interrupts
            //  - softirq: servicing softirqs
            //  - steal: involuntary wait
            //  - guest: running a normal guest
            //  - guest_nice: running a niced guest
            //

            memset(c, 0, sizeof(c));

            for (;;)
            {
                int *p;
                int r;
                char cpunum;

                r = fscanf(f, "cpu%c", &cpunum);

                if (r != 1)
                    break;
                if (cpunum < '0' || cpunum > '3')
                {
                    (void) fscanf(f, "%*[^\n]\n");
                    continue;
                }

                p = c + (cpunum - '0') * 10;

                r = fscanf(f, "%d %d %d %d %d %d %d %d %d %d\n",
                           p, p+1, p+2, p+3, p+4, p+5, p+6, p+7, p+8, p+9);

                if (r != 10)
                    memset(p, 0, 10 * sizeof(int));

            }

            fclose(f);

            //
            // cpu load = (time spent on something else but idle since the last
            // update) / (total time spent since the last update)
            //
            cpu0Load = 0;
            cpu1Load = 0;
            cpu2Load = 0;
            cpu3Load = 0;
            for (i = 0;i < (no_cpus*10); i++)
            {
                l[i] = c[i] - cpuLoadPrev[i];
                if (i < 10)
                    cpu0Load += l[i];
                else if (i < 20)
                    cpu1Load += l[i];
                else if (i < 30)
                    cpu2Load += l[i];
                else
                    cpu3Load += l[i];
                cpuLoadPrev[i] = c[i];
            }
            /* if (debug)
                LOGE("total0 %d  idle0 %d | total1 %d  idle1 %d", cpu0Load,
                l[3+9], cpu1Load, l[3+18]); */
            if (cpu0Load)
                cpu0Load = 100*(cpu0Load-l[3])/cpu0Load;
            if (cpu1Load)
                cpu1Load = 100*(cpu1Load-l[3+10])/cpu1Load;
            if (no_cpus == 4)
            {
                if (cpu2Load)
                    cpu2Load = 100*(cpu2Load-l[3+20])/cpu2Load;
                if (cpu3Load)
                    cpu3Load = 100*(cpu3Load-l[3+30])/cpu3Load;
            }
        }
        else
        {
            PRINTE(bToLog, logFile, "Failed to open /proc/stat");
        }

        // Carveout
        switch (chipId)
        {
            case T30_CHIPID:
                break;
            case AP20_CHIPID:
            default:
                READ_VALUE(bToLog, logFile, CARVEOUT(total_size),
                        &totalCarveoutB);
                READ_VALUE(bToLog, logFile, CARVEOUT(free_size),
                        &freeCarveoutB);
                READ_VALUE(bToLog, logFile, CARVEOUT(free_max),
                        &largestFreeCarveoutBlockB);
                break;
        }

        // GART
        f = fopen("/proc/iovmminfo", "r");
        if (f)
        {
            char tmp[4];
            // add if (blah) {} to get around compiler warning
            if(fscanf(f, "\ngroups\n\t<unnamed> (device: iovmm-%4c)\n\t\tsize: "
                       "%dKiB free: %dKiB largest: %dKiB",
                       &tmp[0], &totalGARTkB, &freeGARTkB,
                       &largestFreeGARTBlockkB)) {}
            fclose(f);
        }
        else
        {
            PRINTE(bToLog, logFile, "Failed to open /proc/iovmminfo");
        }

        // If the largest free GART block is -1, change it to 0.
        if (largestFreeGARTBlockkB == -1)
            largestFreeGARTBlockkB = 0;

        // IRAM
        READ_VALUE(bToLog, logFile, IRAM(total_size), &totalIRAMB);
        READ_VALUE(bToLog, logFile, IRAM(free_size),  &freeIRAMB);
        READ_VALUE(bToLog, logFile, IRAM(free_max),   &largestFreeIRAMBlockB);

        // CPU
        f = fopen("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq", "r");
        if (f)
        {
            (void) fscanf(f, "%d", &currCpuFreq);
            fclose(f);
        }
        // DFS
        READ_VALUE(bToLog, logFile, EMCCLK, &emcClk);
        READ_VALUE(bToLog, logFile, AVPCLK, &avpClk);
        READ_VALUE(bToLog, logFile, VDECLK, &vdeClk);

        {
            char cpu0String[5], cpu1String[5];
            char cpu2String[5], cpu3String[5];
            char lfbRAM[10], lfbCarveout[10], lfbGART[10], lfbIRAM[10];
            if (isCpu0Active)
            {
                snprintf(cpu0String, 5, "%d%%", cpu0Load);
            }
            else
            {
                snprintf(cpu0String, 5, "off");
            }

            if (isCpu1Active)
            {
                snprintf(cpu1String, 5, "%d%%", cpu1Load);
            }
            else
            {
                snprintf(cpu1String, 5, "off");
            }

            if (no_cpus == 4)
            {
                if (isCpu2Active)
                {
                    snprintf(cpu2String, 5, "%d%%", cpu2Load);
                }
                else
                {
                    snprintf(cpu2String, 5, "off");
                }

                if (isCpu3Active)
                {
                    snprintf(cpu3String, 5, "%d%%", cpu3Load);
                }
                else
                {
                    snprintf(cpu3String, 5, "off");
                }
            }
            SmartB2Str(lfbRAM, 10, largestFreeRAMBlockB);
            SmartB2Str(lfbCarveout, 10, largestFreeCarveoutBlockB);
            SmartB2Str(lfbGART, 10, largestFreeGARTBlockkB * 1024);
            SmartB2Str(lfbIRAM, 10, largestFreeIRAMBlockB);
            if (no_cpus == 2)
            {
                switch (chipId)
                {
                    case T30_CHIPID:
                        PRINTE(bToLog, logFile, "RAM %d/%dMB (lfb %dx%s) IRAM "
                                "%d/%dkB(lfb %s) cpu [%s,%s]@%d EMC %d AVP %d "
                                "VDE %d EDP limit %d",
                            kB2MB(totalRAMkB-freeRAMkB-buffersRAMkB-cachedRAMkB),
                            kB2MB(totalRAMkB), numLargestRAMBlock, lfbRAM,
                            B2kB(totalIRAMB-freeIRAMB),
                            B2kB(totalIRAMB), lfbIRAM, cpu0String, cpu1String,
                            currCpuFreq/FREQUENCY_CONVERT,
                            emcClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            avpClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            vdeClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            edp_limit);
                        break;

                    case AP20_CHIPID:
                        // intentionally fall through
                    default:
                        PRINTE(bToLog, logFile, "RAM %d/%dMB (lfb %dx%s) "
                                "Carveout %d/%dMB (lfb %s) GART %d/%dMB (lfb %s)"
                                " IRAM %d/%dkB(lfb %s) cpu [%s,%s]@%d EMC %d AVP"
                                " %d VDE %d EDP limit %d",
                            kB2MB(totalRAMkB-freeRAMkB-buffersRAMkB-cachedRAMkB),
                            kB2MB(totalRAMkB), numLargestRAMBlock, lfbRAM,
                            B2MB(totalCarveoutB-freeCarveoutB),
                            B2MB(totalCarveoutB), lfbCarveout,
                            kB2MB(totalGARTkB-freeGARTkB), kB2MB(totalGARTkB),
                            lfbGART, B2kB(totalIRAMB-freeIRAMB), B2kB(totalIRAMB),
                            lfbIRAM, cpu0String, cpu1String,
                            currCpuFreq/FREQUENCY_CONVERT,
                            emcClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            avpClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            vdeClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            edp_limit);
                        break;
                }

            }
            else
            {
                switch (chipId)
                {
                    case T30_CHIPID:
                        PRINTE(bToLog, logFile, "RAM %d/%dMB (lfb %dx%s) IRAM "
                                "%d/%dkB(lfb %s) cpu [%s,%s,%s,%s]@%d EMC %d AVP"
                                " %d VDE %d EDP limit %d",
                            kB2MB(totalRAMkB-freeRAMkB-buffersRAMkB-cachedRAMkB),
                            kB2MB(totalRAMkB), numLargestRAMBlock, lfbRAM,
                            B2kB(totalIRAMB-freeIRAMB), B2kB(totalIRAMB),
                            lfbIRAM, cpu0String, cpu1String, cpu2String,
                            cpu3String, currCpuFreq/FREQUENCY_CONVERT,
                            emcClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            avpClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            vdeClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            edp_limit);
                        break;

                    case AP20_CHIPID:
                        // intentionally fall through
                    default:
                        PRINTE(bToLog, logFile, "RAM %d/%dMB (lfb %dx%s) "
                                "Carveout %d/%dMB (lfb %s) GART %d/%dMB (lfb %s)"
                                " IRAM %d/%dkB(lfb %s) cpu [%s,%s,%s,%s]@%d "
                                "EMC %d AVP %d VDE %d EDP limit %d",
                            kB2MB(totalRAMkB-freeRAMkB-buffersRAMkB-cachedRAMkB),
                            kB2MB(totalRAMkB), numLargestRAMBlock, lfbRAM,
                            B2MB(totalCarveoutB-freeCarveoutB),
                            B2MB(totalCarveoutB), lfbCarveout,
                            kB2MB(totalGARTkB-freeGARTkB), kB2MB(totalGARTkB),
                            lfbGART, B2kB(totalIRAMB-freeIRAMB),
                            B2kB(totalIRAMB), lfbIRAM, cpu0String, cpu1String,
                            cpu2String, cpu3String, currCpuFreq/FREQUENCY_CONVERT,
                            emcClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            avpClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            vdeClk/(FREQUENCY_CONVERT*FREQUENCY_CONVERT),
                            edp_limit);
                        break;
                }
            }
        }

        // fflush stdout (on LDK) to make the output redirectable.
        logFlush();
#ifndef GTK_OUTPUT
        usleep(sleepMS*1000);
#endif
    }

    return 0;
}

/* example contents of /proc/meminfo
MemTotal:         450164 kB
MemFree:          269628 kB
Buffers:            2320 kB
Cached:            69008 kB
SwapCached:            0 kB
Active:            89476 kB
Inactive:          63612 kB
Active(anon):      82272 kB
Inactive(anon):        0 kB
Active(file):       7204 kB
Inactive(file):    63612 kB
Unevictable:           0 kB
Mlocked:               0 kB
SwapTotal:             0 kB
SwapFree:              0 kB
Dirty:                 0 kB
Writeback:             0 kB
AnonPages:         81764 kB
Mapped:            35148 kB
Slab:               5204 kB
SReclaimable:       1760 kB
SUnreclaim:         3444 kB
PageTables:         4316 kB
NFS_Unstable:          0 kB
Bounce:                0 kB
WritebackTmp:          0 kB
CommitLimit:      225080 kB
Committed_AS:    1054316 kB
VmallocTotal:     450560 kB
VmallocUsed:       45964 kB
VmallocChunk:     340056 kB

http://www.linuxweblog.com/meminfo
    * MemTotal: Total usable ram (i.e. physical ram minus a few reserved bits
      and the kernel binary code)
    * MemFree: Is sum of LowFree+HighFree (overall stat)
    * MemShared: 0 is here for compat reasons but always zero.
    * Buffers: Memory in buffer cache. mostly useless as metric nowadays
    * Cached: Memory in the pagecache (diskcache) minus SwapCache
    * SwapCache: Memory that once was swapped out, is swapped back in but still
      also is in the swapfile (if memory is needed it doesn't need to be swapped
      out AGAIN because it is already in the swapfile. This saves I/O)

VM splits the cache pages into "active" and "inactive" memory. The idea is that
if you need memory and some cache needs to be sacrificed for that, you take it
from inactive since that's expected to be not used. The vm checks what is used
on a regular basis and moves stuff around. When you use memory, the CPU sets a
bit in the pagetable and the VM checks that bit occasionally, and based on that,
it can move pages back to active. And within active there's an order of "longest
ago not used" (roughly, it's a little more complex in reality).
    * Active: Memory that has been used more recently and usually not reclaimed
      unless absolutely necessary.
    * Inact_dirty: Dirty means "might need writing to disk or swap." Takes more
      work to free. Examples might be files that have not been written to yet.
      They aren't written to memory too soon in order to keep the I/O down. For
      instance, if you're writing logs, it might be better to wait until you have
      a complete log ready before sending it to disk.
    * Inact_clean: Assumed to be easily freeable. The kernel will try to keep
      some clean stuff around always to have a bit of breathing room.
    * Inact_target: Just a goal metric the kernel uses for making sure there are
      enough inactive pages around. When exceeded, the kernel will not do work to
      move pages from active to inactive. A page can also get inactive in a few
      other ways, e.g. if you do a long sequential I/O, the kernel assumes you're
      not going to use that memory and makes it inactive preventively. So you can
      get more inactive pages than the target because the kernel marks some cache
      as "more likely to be never used" and lets it cheat in the "last used"
      order.
    * HighTotal: is the total amount of memory in the high region. Highmem is all
      memory above (approx) 860MB of physical RAM. Kernel uses indirect tricks to
      access the high memory region. Data cache can go in this memory region.
    * LowTotal: The total amount of non-highmem memory.
    * LowFree: The amount of free memory of the low memory region. This is the
      memory the kernel can address directly. All kernel datastructures need to go
      into low memory.
    * SwapTotal: Total amount of physical swap memory.
    * SwapFree: Total amount of swap memory free.
    * Committed_AS: An estimate of how much RAM you would need to make a 99.99%
      guarantee that there never is OOM (out of memory) for this workload. Normally
      the kernel will overcommit memory. The Committed_AS is a guesstimate of how
      much RAM/swap you would need worst-case.
*/
