output "application_name" {
  description = "Nome da aplicação Elastic Beanstalk"
  value       = aws_elastic_beanstalk_application.app.name
}

output "environment_name" {
  description = "Nome do ambiente Elastic Beanstalk"
  value       = aws_elastic_beanstalk_environment.env.name
}

output "cname" {
  description = "URL CNAME gerada para acesso à aplicação"
  value       = aws_elastic_beanstalk_environment.env.cname
}

output "endpoint_url" {
  description = "URL do Endpoint do ambiente Elastic Beanstalk"
  value       = aws_elastic_beanstalk_environment.env.endpoint_url
}
