#!/bin/bash

if [ $# -eq 1 ]
then
	u=$1
else
	u=${USER}
fi

mv webapp/target/webapp.war webapp/target/app.war

scp webapp/target/app.war ${u}@pampero.it.itba.edu.ar:/home/${u}/.

ssh ${u}@pampero.it.itba.edu.ar << EOF
	export SSHPASS=***REMOVED***
	sshpass -e sftp -oBatchMode=no -b - paw-2022a-06@10.16.1.110 << !
	cd web
	put app.war
	bye
!
EOF
