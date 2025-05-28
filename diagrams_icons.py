# OnPrem
from diagrams.onprem.aggregator import Fluentd, Vector
from diagrams.onprem.analytics import Beam, Databricks, Dbt, Dremio, Flink, Hadoop, Hive, Metabase, Norikra, Powerbi, Presto, Singer, Spark, Storm, Superset, Tableau, Trino
from diagrams.onprem.auth import Boundary, BuzzfeedSso, Oauth2Proxy
from diagrams.onprem.cd import Spinnaker, TektonCli, Tekton
from diagrams.onprem.certificates import CertManager, LetsEncrypt
from diagrams.onprem.ci import Circleci, Concourseci, Droneci, GithubActions, Gitlabci, Jenkins, Teamcity, Travisci, Zuulci
from diagrams.onprem.client import Client, User, Users
from diagrams.onprem.compute import Nomad, Server
from diagrams.onprem.container import Containerd, Crio, Docker, Firecracker, Gvisor, K3S, Lxc, Rkt
from diagrams.onprem.database import Cassandra, Clickhouse, Cockroachdb, Couchbase, Couchdb, Dgraph, Druid, Hbase, Influxdb, Janusgraph, Mariadb, Mongodb, Mssql, Mysql, Neo4J, Oracle, Postgresql, Scylla
from diagrams.onprem.dns import Coredns, Powerdns
from diagrams.onprem.etl import Embulk
from diagrams.onprem.gitops import Argocd, Flagger, Flux
from diagrams.onprem.groupware import Nextcloud
from diagrams.onprem.iac import Ansible, Atlantis, Awx, Pulumi, Puppet, Terraform
from diagrams.onprem.identity import Dex
from diagrams.onprem.inmemory import Aerospike, Hazelcast, Memcached, Redis
from diagrams.onprem.logging import Fluentbit, Graylog, Loki, Rsyslog, SyslogNg
from diagrams.onprem.messaging import Centrifugo
from diagrams.onprem.mlops import Mlflow, Polyaxon
from diagrams.onprem.monitoring import Cortex, Datadog, Dynatrace, Grafana, Humio, Mimir, Nagios, Newrelic, PrometheusOperator, Prometheus, Sentry, Splunk, Thanos, Zabbix
from diagrams.onprem.network import Ambassador, Apache, Bind9, Caddy, Consul, Envoy, Etcd, Glassfish, Gunicorn, Haproxy, Internet, Istio, Jbossas, Jetty, Kong, Linkerd, Mikrotik, Nginx, Ocelot, OpenServiceMesh, Opnsense, Pfsense, Pomerium, Powerdns, Tomcat, Traefik, Tyk, Vyos, Wildfly, Yarp, Zookeeper
from diagrams.onprem.proxmox import Pve
from diagrams.onprem.queue import Activemq, Celery, Emqx, Kafka, Nats, Rabbitmq, Zeromq
from diagrams.onprem.registry import Harbor, Jfrog
from diagrams.onprem.search import Solr
from diagrams.onprem.security import Bitwarden, Trivy, Vault
from diagrams.onprem.storage import CephOsd, Ceph, Glusterfs, Portworx
from diagrams.onprem.tracing import Jaeger, Tempo
from diagrams.onprem.vcs import Git, Gitea, Github, Gitlab, Svn
from diagrams.onprem.workflow import Airflow, Digdag, Kubeflow, Nifi

# Programming
from diagrams.programming.flowchart import Action, Collate, Database, Decision, Delay, Display, Document, InputOutput, Inspection, InternalStorage, LoopLimit, ManualInput, ManualLoop, Merge, MultipleDocuments, OffPageConnectorLeft, OffPageConnectorRight, Or, PredefinedProcess, Preparation, Sort, StartEnd, StoredData, SummingJunction
from diagrams.programming.framework import Angular, Backbone, Camel, Django, Dotnet, Ember, Fastapi, Flask, Flutter, Graphql, Hibernate, Jhipster, Laravel, Micronaut, Nextjs, Phoenix, Quarkus, Rails, React, Spring, Sqlpage, Starlette, Svelte, Vercel, Vue
from diagrams.programming.language import Bash, C, Cpp, Csharp, Dart, Elixir, Erlang, Go, Java, Javascript, Kotlin, Latex, Matlab, Nodejs, Php, Python, R, Ruby, Rust, Scala, Sql, Swift, Typescript
from diagrams.programming.runtime import Dapr

