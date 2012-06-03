CFLAGS_LOC = -Wall `pkg-config --cflags gtk+-2.0`
LDFLAGS_LOC = `pkg-config --libs gtk+-2.0`

CC = gcc

OBJS = main.o stats.o

all: tegrastats-gtk 
	
.c.o :
	$(CC) $(CFLAGS) $(CFLAGS_LOC) -c $<

tegrastats-gtk:	$(OBJS)
	$(CC) $(GTK_LIB) $(X11_LIB) $(OBJS) -o $@ $(LDFLAGS) $(LDFLAGS_LOC)

clean:
	rm -f *.o *~ helloworld
