output "ec2_instance_profile_name" {
  description = "Nome do Instance Profile para o EC2 do Elastic Beanstalk"
  value       = aws_iam_instance_profile.eb_ec2_profile.name
}

output "ec2_role_arn" {
  description = "ARN da Role do EC2 do Elastic Beanstalk"
  value       = aws_iam_role.eb_ec2_role.arn
}

output "service_role_arn" {
  description = "ARN da Role de Servico do Elastic Beanstalk"
  value       = aws_iam_role.eb_service_role.arn
}
