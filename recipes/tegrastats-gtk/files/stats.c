#include <gtk/gtk.h>
void GetStats(char * str);
gboolean
UpdateTitle (gpointer user_data)
{
	gchar title[255];

	(void)GetStats(title);
	gtk_window_set_title ((GtkWindow*)user_data, title);
	//we want to start a new intervall
	return 1;
}

void
destroy (void)
{
  gtk_main_quit ();
}

int
main (int argc, char *argv[])
{
  GtkWidget *window;

  gtk_init (&argc, &argv);

  window = gtk_window_new (GTK_WINDOW_TOPLEVEL);
  gtk_signal_connect (GTK_OBJECT (window), "destroy",
		      GTK_SIGNAL_FUNC (destroy), NULL);
  gtk_container_border_width (GTK_CONTAINER (window), 10);
  gtk_window_set_default_size ((GtkWindow*)window, 1200, 0);

  (void)UpdateTitle(GTK_CONTAINER (window));

  //update title every 2 seconds
  g_timeout_add_seconds (1, UpdateTitle, window);
  gtk_widget_show (window);

  gtk_main ();

  return 0;
}
