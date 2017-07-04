
README file for JADE, Version $Name$

INTRODUCTION
============
This package contains a Java framework to build agent-based systems
according to FIPA standard specifications.

LICENSE
=======
see file License.

FEEDBACK
=======
As you know already, this is still an on-going project. 
We are still working on the framework and new versions will be distributed 
as soon as available.
Your feedback as users is very important to us. Please, if you have new 
requirements that you would like to see implemented or if you have examples 
of usage or if you discover some bugs, send us information.
Check the website http://jade.tilab.com/
for how to report bugs and send suggestions.  

SYSTEM REQUIREMENTS
===================
To build the framework a complete Java programming environment is
needed. At least a Java Development Kit version 1.4 is required. 


KNOWN BUGS
==========
  see http://jade.tilab.com/  ('Bugs' page)  for the full list of reported bugs


CONTACT
=======
Fabio Bellifemine - TILab S.p.A.
e-mail: bellifemine@tilab.com 


INSTALLATION AND TEST
==============================
You can download JADE in source form and recompile it yourself, 
or get the pre-compiled binaries (actually they are JAR files). 
The following is an excerpt from the programmer's guide.

5.1 Software requirements
=========================
The only software requirement to execute the system is the Java Run Time 
Environment version 1.4
Further to the Java Compiler version 1.4, to build the system, the JavaCC 
parser generator (version 3.2 of JavaCC since JADE 3.2),
the IDL to Java translator idltojava 
(available from the Sun Developer Connection) are also needed. However, 
pre-built IDL stubs and Java parser classes are included with the JADE 
source distribution such that the Java compiler is sufficient to build 
the full system, finally the ANT program to compile the source code of 
JADE with build.xml file, ANT is available from http://jakarta.apache.org.

5.2 Getting the software
========================
All the software is distributed under the LGPL license limitations. 
It can be downloaded from the JADE web site 
http://jade.tilab.com/ Five compressed files are available:
1. the source code of JADE
2. the source code of the examples
3. the documentation, including the javadoc of the JADE API and 
this programmer's guide
4. the binary of JADE, i.e. the jar files with all the Java classes
5. a full distribution with all the previous files


5.3 Running JADE from the binary distribution
=============================================
Having uncompressed the archive file, a directory tree is generated whose 
root is jade and with a lib subdirectory. This subdirectory contains some 
JAR files that have to be added to the CLASSPATH environment variable.
Having set the classpath, the following command can be used to launch the 
main container of the platform. The main container is composed of the DF 
agent, the AMS agent, and an RMI registry (that is used by JADE for 
intra-platform communication).
        java jade.Boot [options] [Agent list]
Additional agent containers can be then launched on the same host, or on 
remote hosts, that connect themselves with the main container of the Agent 
Platform, resulting in a distributed system that seems a single Agent 
Platform from the outside.
An Agent Container can be started using the command:
        java jade.Boot -container [options] [Agent list]

An alternative way of launching JADE is the following command:
        java -jar lib\jade.jar -nomtp [options] [Agent list]

see the Administrator's guide for the list of 
options available from the command line


5.3.2 Launching agents from the command line
============================================
A list of agents can be launched directly from the command line. As described
above, the [Agent list] part of the command is a sequence of strings separated
by a space.
Each string is broken in two parts separated by a colon ':' character. The 
substring before the colon is taken as the agent name, whereas the substring 
after the colon is the name of the Java class implementing the agent. This 
class will be dynamically loaded by the Agent Container.
For example, a string Peter:myAgent means "create a new agent named Peter 
whose implementation is an object of class myAgent". The name of the class 
must be fully qualified, (e.g. Peter:myPackage.myAgent) and will be searched 
for according to CLASSPATH definition.

5.3.3 Example
=============
First of all set the CLASSPATH to include the JAR files in the lib 
subdirectory and the current directory. For instance, for Windows 9x/NT use 
the following command:
set CLASSPATH=%CLASSPATH%;.;c:\jade\lib\jade.jar;
                c:\jade\lib\jadeTools.jar; c:\jade\lib\Base64.jar;
		c:\jade\lib\http.jar
Execute the following command to start the main-container of the platform. 
Let's suppose that the hostname of this machine is "kim.cselt.it"
prompt> java jade.Boot -gui 
Execute the following command to start an agent container on another machine,
by telling it to join the AgentPlatform, called "facts" running on the host 
"kim.cselt.it", and start one agent (you must download and compile the 
examples agents to do that): 
prompt> java jade.Boot -host kim.cselt.it -container
          sender1:examples.receivers.AgentSender 
where "sender1" is the name of the agent,  
while examples.receivers.AgentSender is the code that implements the agent.
Execute the following command on a third machine to start another agent 
container telling it to join the Agent Platform, called "facts" running on 
the host "kim.cselt.it", and then start two agents. 
prompt> java jade.Boot -host kim.cselt.it -container 
          receiver2:examples.receivers.AgentReceiver 
          sender2:examples.receivers.AgentSender
where the agent named sender2 is implemented by the class 
examples.receivers.AgentSender, while the agent named receiver2 is 
implemented by the class examples.receivers.AgentReceiver.

5.4 Building JADE from the source distribution
==============================================
If you downloaded the source code of JADE, you can compile it by using the 
"ant" program, a platform independent version of make.
The file "build.xml" in the JADE root directory is the input file for ant. 
The "ant" program must 
be installed on your computer, it can be downloaded from the Jakarta Project 
at the Apache web site: <http://jakarta.apache.org/>.

5.4.1 Building the JADE framework
=================================
Just type
ant jade
you run ant on build.xml file in the root directory.
You will end up with all JADE classes in a classes subdirectory. You can add 
that directory to your CLASSPATH and make sure that everything is OK by 
running JADE, as described in the previous section.

5.4.2 Building JADE libraries
============================
Type:
ant lib
This will remove the content of the classes directory and will create some 
JAR files in the lib directory. These JAR files are just the same you get 
from the binary distribution. See section 5.3 for a description on how to 
run JADE when you have built the JAR files. 
NOTE: jade/lib/Base64.jar is only needed if you want to use the support for JADE 
      serialization and trasmitting sequences of bytes within an ACLMessage. In all
      other cases, it is not necessary adding it to CLASSPATH .

5.4.3 Building JADE HTML documentation
======================================
Type:
ant doc
You will end up with Javadoc generated HTML pages, integrated within the 
overall documentation. Beware that the Programmer's Guide is a PDF file that 
cannot be generated at your site, but you must download it (it is, of course,
in the JADE documentation distribution).

5.4.4 Building JADE examples and demo application
=================================================
If you downloaded the examples/demo archive and have unpacked it within the 
same source tree, you will have to set your CLASSPATH to contain either the 
classes directory or the JAR files in the lib directory, depending on your 
JADE distribution, and then type:
ant examples
In order to compile the Jess-based example, it is necessary to have the JESS 
system, to set the CLASSPATH to include it and to set JESS_HOME. The example 
can be compiled by typing:
ant jessexample

5.4.5 Cleaning up the source tree
=================================
If you type:
ant clean
you will remove all generated files (classes, HTML pages, JAR files, etc.) 
from the source tree. If you use makefiles, you will find some other make 
targets you can use. Feel free to try them, especially if you are modifying 
JADE source code, but be aware that these other make targets are for internal 
use only, so they have not been documented.

5.6 IIOP support and inter-platform messaging
=============================================
JADE supports FIPA compliant IIIOP communication for inter-platform agent 
communication. This mechanism is used both to communicate with another JADE 
platform and with a non-JADE platform. JADE achieves complete transparency 
in message passing even when multiple agent platforms are involved, so agent 
developers need not worry about IIOP: JADE selects local Java events, RMI or 
CORBA/IIOP automatically on behalf of the application.
The only issue application developers and platform administrators must be 
aware-of is agent naming. An agent identifier, in fact, must include a set 
of URL representing the addresses where it can be contacted.
Every JADE agent inherits the addresses of its platform and its AID 
(including the addresses) is fully generated automatically by JADE when 
messages are sent externally to the platform. 
Because most of the CORBA ORB implementations (including the one used by 
JADE) do not yet allow to choose meaningful words as object keys, JADE 
resorts to the alternate naming scheme, adopted also by FIPA, 
using OMG standard stringified IOR as agent addresses. A valid agent address 
can be both an URL like iiop://fipa.org:50/acc and an IOR as 
IOR:000000000000001649444c644f4)
The IOR-based representation and the URL-based one are exactly equivalent, 
the URL being far more readable for humans than the IOR. JADE generates 
IOR-based addresses but can also deal with URL-based ones as long as the URL 
contains only printable characters (i.e. has been created by an ORB allowing 
explicit object key assignment) that can be parsed by a FIPA-compliant parser.
When starting up, JADE platform prints its IOR both on the standard output 
and in a ASCII file named JADE.IOR, located in the current directory; the 
URL for the platform (containing a binary string in the file part) is also 
written to the file JADE.URL in the current directory. Every agent address 
automatically includes this platform IOR that should be distributed to remote 
platforms in order to allow remote agents to send messages to your JADE 
agents. The distribution mechanism is not specified by FIPA and is 
application dependent. 


