
keytool -export -keystore keystore0 -alias jade-main -storepass mysecretpassword -rfc -file main.cer

keytool -printcert -file main.cer

dir
pause
