The file agentgui in this directory represents a start script 
that can be used on Linux systems and install Agent.GUI as a
service that can be executed as 'SERVER' (Slave or Master) or 
as an embedded system that start after system boot.

agentgui-monolithic.service is a SystemD system service unit file for the monolithic variant 
agentgui.service is a SystemD system service unit file for the osgi variant

A system user "agentgui" is used/necessary.

The .service files can be placed into /etc/systemd/system/.  Run `sudo systemctl daemon-reload` and then `systemctl start agentgui`

