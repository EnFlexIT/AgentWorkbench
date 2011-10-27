
keytool -import -keystore mytruststore -alias jade-main -storepass mysecretpassword -file ..\main\main.cer
pause

keytool -import -keystore mytruststore -alias jade-cont1 -storepass mysecretpassword -file ..\cont1\cont1.cer
pause

keytool -list -keystore mytruststore -storepass mysecretpassword 

pause

copy mytruststore ..\cont1\

pause
