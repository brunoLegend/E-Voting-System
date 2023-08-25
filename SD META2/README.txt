Central_Server is the RMI Server: It receives the IP address where the RMI is running as an argument.

Mesa_voto is the Multicast Server which receives:

Department name
Maximum number of terminals
IP address of the server hosting the RMI
Terminal receives as an argument:

Multicast address, which is always 224.3.2. The last number depends on the mesa (table) to which it will connect.
In central_server.java, there's a config function that associates each department name with a number. For example, for DEEC, the multicast address is 224.3.2.2, and for DEI, it's 224.3.2.1.
Admin_Console receives the IP address where the RMI server is running as an argument.

For Milestone 2:

Start the central_server first.
Then start Tomcat.
The admin credentials are hardcoded in the code, so it should be possible to log in with Username: Admin, Password: Admin.
