
 SSL IMTP Example for JICP
==================

Mutual-Authentication
---------------------------
perform the following steps in sequence:

into the directory ./main/  launch: 
1-createKeyStore0.bat
2-exportCert0.bat

into the directory ./cont1/  launch: 
1-createKeyStore1.bat
2-exportCert1.bat

into the directory ./main/  launch:
3-importTrusted.bat
4-showTrusted.bat

into the directory ./main/  launch:
 start-main.bat 
then (after a few seconds)
into the directory ./cont1/  launch:
 start-cont1.bat 

As excercise, you can create a new
directory for the container cont2.
DO not forget to create keystore 
and truststore for it.


 No-Authentication
-----------------------
perform the following steps in sequence.

into the directory ./NoAuth/  launch: 
 NoAuth-main.bat
then (after a few seconds)
 NoAuth-cont1.bat


-
by Giosue.Vitaglione, on 2004-11-18
