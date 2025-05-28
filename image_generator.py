import diagrams
from diagrams import Cluster, Diagram
from diagrams.aws.compute import EC2, ECS, Lambda
from diagrams.aws.database import RDS, ElastiCache, DocumentDB
from diagrams.aws.network import ELB, Route53
from diagrams.aws.storage import S3
from diagrams.aws.security import IAM

with Diagram("Jogging App Architecture on AWS", show=False, filename="jogging_aws"):
    with Cluster("VPC"):
        with Cluster("Application Tier"):
            lb = ELB("Load Balancer")
            ecs = ECS("ECS Fargate\n(Spring Boot App)")
            lb >> ecs

        with Cluster("Data Tier"):
            db = DocumentDB("DocumentDB\n(MongoDB)")
            cache = ElastiCache("ElastiCache\n(Redis)")
            ecs >> db
            ecs >> cache

        with Cluster("Storage Tier"):
            s3 = S3("S3\n(Logs, Static Assets)")
            ecs >> s3

        route53 = Route53("Route53\n(DNS)")
        internet = diagrams.Node("Internet")
        internet >> route53 >> lb

        iam = IAM("IAM Roles")
        ecs >> iam
        db >> iam
        s3 >> iam