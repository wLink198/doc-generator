from diagrams import Diagram, Cluster
from diagrams.onprem.database import Mongodb
from diagrams.programming.framework import Spring
from diagrams.custom import Custom

with Diagram("Jogging App Architecture", show=False):
    with Cluster("Web/API Backend"):
        step_controller = Custom("StepController", "./layer.png")
        step_service = Custom("StepService", "./layer.png")
        spring_boot = Spring("Spring Boot")

        spring_boot >> step_controller >> step_service

    mongodb = Mongodb("MongoDB")

    with Cluster("Caching"):
        spring_cache = Spring("Spring Cache")

    step_service >> spring_cache
    spring_cache >> mongodb
