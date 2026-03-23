--Build image on docker :
   1. docker build -t maven-tests .
--compose environment and network 
    2. docker compose up
-To run maven tests in container
    --one Way:
        //network created and docker compose up done
        //run image as container
        3. docker run -it --network maventests-runoncontainer_seleniumgrid4 --name testrunnerMC1 
        -e HOST_URL=http://HubService:4444/wd/hub -e OUTPUT_DIR=/app/output testrunner
    --2nd OR
        //create container without running
        3. docker create -v ./output:/app/output --name testrunnerMC1 maventests-runoncontainer-testrunner 
        4. docker network connect maventests-runoncontainer_seleniumgrid4 testrunnerMC1  //connect with network
        5. docker start -ai testrunnerMC1 //start and execute container

---How to see the volumes:
one way on windows poweshell
    docker exec -it <container id/name> /bin/bash
    $ls /app/output
in docker desktop:
    go to the container> exec> cd /app/output    

--normally bind mount should : \\wsl.localhost\docker-desktop\mnt\docker-desktop-disk\data\docker\containers