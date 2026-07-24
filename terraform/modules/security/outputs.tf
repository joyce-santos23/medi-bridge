output "beanstalk_security_group_id" {
  description = "ID do Security Group do Elastic Beanstalk"
  value       = aws_security_group.beanstalk_sg.id
}

output "rds_security_group_id" {
  description = "ID do Security Group do RDS PostgreSQL"
  value       = aws_security_group.rds_sg.id
}

output "vpc_id" {
  description = "ID da VPC onde os Security Groups foram criados"
  value       = local.vpc_id
}

