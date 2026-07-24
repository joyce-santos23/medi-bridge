output "beanstalk_cname" {
  description = "URL publica da aplicacao no Elastic Beanstalk"
  value       = module.beanstalk.cname
}

output "beanstalk_environment_name" {
  description = "Nome do ambiente Elastic Beanstalk"
  value       = module.beanstalk.environment_name
}

output "rds_endpoint" {
  description = "Endpoint do banco de dados RDS PostgreSQL"
  value       = module.rds.db_endpoint
}
