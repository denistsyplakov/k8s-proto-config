# About 

This is a prototype that demonstrates best 
practices of Spring boot application configuration in 
Docker and Kubernetes environments. 

The main idea of this prototype is to have a unified way to inject a configuration into an application in all 3 
cases from the list below:
 - Standalone non-dockerized run via `java -jar app.jar` or `gradle bootRun` or IDE Green-Run-Button
 - Launch via `docker run` 
 - Launch in a K8S environment via `kubectl apply -f deployment.yaml` 
 

# Ways to run the prototype

Before you start. In the root folder copy the content of `application.properties.template` file 
to `application.properties` file and set your own values. 
Do not commit this new file to git! In real life it usually contains some secret values.

## Non-containerized, local development environment

Run 

`gradle build`

`java -jar build/libs/ds-0.0.1.jar` 

Or just 

`gradle bootRun`

This is it, the new config values will be used.

## Dockerized

### Step1: build Docker image 

In the root folder run

`gradle clean build` 

`docker build -t image-name .`

### Step2: Run a container

To run with the default properties from `src/main/resources/application.properties` run

`docker run image-name` 

To override it with your local properties from the project root folder run 

`docker run  -v %cd%:/config image-name` 

See also the helper script `runInDocker.cmd` (in the UNIX world %cd% should be replaced with \`pwd`) 

In the production mode mounting the whole project source tree to some folder inside the container looks a bit hacky, but for a dev run it is OK. 

If you want to use this "single docker container" in production 
(e.g., you need to run a single container with Docker and inject configuration values into it),
you need to create a directory with one `application.properties` file, and mount it into `/config` directory. 

## Kubernetes

Now let's run this in K8S cluster. 
Let's assume we have K8S deployment which is deployed from our `image-name` image 
and we need to configure it with custom `application.properties`

### Step1: build image and push it to your project docker hub

`gradle clean build` 

`docker build -t image-name .`

`docker push image-name`

### Step2: create K8S configmap

Here we are creating configmap manually from the command line. 
Obviously in a real production environment this should be automated 
with some framework, for instance, Terraform. But let's keep it simple

`kubectl create configmap application.properties --from-file=application.properties`   

### Step3: Run application in K8S

In the directory `src/main/k8s` run `kubectl aplly -f deployment.sh`. This will create deployment from a docker image 
(if you want to run sample code yourself, you need to adjust the image name). This deployment will use configmap 
that you have created on the step 2. The deployment will use application.properties file that k8s will place in 
the `/config` directory. 

# Summary

In the example above we have two sets of configuration properties.

 - First - default, embedded into the jar file
 - Second - externalized config contains properties that are specific for a given environment. 
 
 These properties could be used in a unified way in 3 cases: non-dockerized run, 
 via `docker run` command and in k8s cluster.  
 
I hope this will help you to avoid zillions of ${ENV_VARIABLE_100500} variables. 