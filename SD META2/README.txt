.Central_Server é o RMI server: Recebe como argumento o ip onde o rmi está a correr

.Mesa_voto é o servidor Multicast recebe:
1-Nome do departamento
2-Numero máximo de terminais
3-ip do servidor que tem o rmi

.Terminal recebe como argumento

1-endereço multicast, é sempre 224.3.2. o ultimo número depende da mesa onde se vai ligar
No central_server.java existe uma função config que à frente de cada nome de departamento contém o seu número,
por exemplo para o DEEC o endereço multicast é o 224.3.2.2 e para o DEI é o 224.3.2.1

.Admin_Console
recebe como argumento o ip onde está a correr o servidorRmi


Para a meta2:
Apenas correr o central_server em primeiro lugar e de seguida o tomcat
O admin está no código por isso já deve ser possivel fazer login com-> Username:Admin Password:Admin