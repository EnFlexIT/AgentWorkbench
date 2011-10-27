
keytool -export -keystore keystore1 -alias jade-cont-1 -storepass mysecretpassword -rfc -file cont1.cer

keytool -printcert -file cont1.cer

dir
pause
