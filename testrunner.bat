one Way:
//network created and docker compose up done
docker compose up
//run image as container
docker run -it --network maventests-runoncontainer_seleniumgrid4 --name testrunnercontainer1 -e HOST_URL=http://HubService:4444/wd/hub -e OUTPUT_DIR=/app/output -v ${pwd}/output:/app/output testrunner
2nd Way:
//create container without running
docker create -v testunnerVol:/app/output -v ${pwd}/output:/app/output --name testrunnerC1 maventests-runoncontainer-testrunner 
docker network connect maventests-runoncontainer_seleniumgrid4 testrunnerC1  //connect with network
docker start -ai testrunnerC1 //start and execute container
docker run -it --network maventests-runoncontainer_seleniumgrid4 --name testrunnercontainer1 testrunner

---How to see the volumes:
one way on windows poweshell
    docker exec -it <container id/name> /bin/bash
    $ls /app/output
in docker desktop:
    go to the container> exec> cd /app/output    