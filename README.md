# About 

This is a prototype that demonstrates best 
practices of Spring boot application application configuration in 
Docker and Kubernetes environments  

Main idea is to have unified way to inject configuration into application in all 3 cases
 - Standalone non dockerized run via `java -jar app.jar` or `gradle bootRun` or IDE Green-Run-Button
 - Launch via `docker run` 
 - Launch in K8S environment via `kubectl apply -f deployment.yaml` 
 

# Steps to run prototype

Before any case: in the root folder 
rename `application.properties.template` to `application.properties` and set your own values. 
Do not commit this new file to git.

## Non containerized, local development environment

Run 

`gradle build
java -jar build/libs/ds-0.0.1.jar` 

Or just 

`gradle bootRun`

very simple

## Dockerized

### Step1: build image 

`gradle clean build 
docker build -t image-name .`

### Step2: Run container

To run with default properties from `src/main/resources/application.properties` run

`docker run image-name`

To override it with your local properties from the project root folder run 

`docker run  -v %cd%:/config image-name` 

See also helper script `runInDocker.cmd` (in the UNIX world %cd% should be replaced with \`pwd`) 

Well, in dev mode this looks a bit hacky, but for debug test run it is OK. 
If you you want to use this in production 
(e.g. you need to run single container with Docker and inject configuration)
you need to create a directory with one `application.properties` file and mount 
it to `/config` then `/start.sh` script will do the rest. 

## Kubernetes

OK. Now let's run this in production mode. 
Let's assume we have deployment which is deployed from our `image-name` image 
and we need to configure it with custom `application.properties`

### Step1: build image and docker hub

`gradle clean build 
docker build -t image-name .
docker push image-name`

### Step2: create K8S configmap

Here we are creating configmap manually with command. Obviously in real production environment this should be automated 
with some framework. But here let's keep it simple

`kubectl create configmap application.properties --from-file=application.properties`   

### Step3: Run application in K8S

In the directory `src/main/k8s` run `kubectl aplly -f .`. This will create deployment from our docker image 
(if you want to run it yourself, you need to adjust image name). This deployment will use configmap 
that you have created on the step 2. The deployment will use application.properties file that k8s will place in 
the `/config` directory. 

# Summary

In the example above we have two sets of configuration properties.

 - First - default, embedded into jar file
 - Second - second contains properties that are specific for a given environment. 
 Theses properties could be used in a unified way in 3 cases: non-dockerized run, 
 via `docker run` command and in k8s cluster.  
 
I hope this will help you to avoid zillions of ${ENV_VARIABLE_10050} variables 