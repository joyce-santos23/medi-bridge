variable "app_name" {
  description = "Nome da aplicação"
  type        = string
}

variable "environment" {
  description = "Nome do ambiente"
  type        = string
}

variable "vpc_id" {
  description = "ID da VPC onde o Elastic Beanstalk sera implantado"
  type        = string
}

variable "subnets" {
  description = "Lista de subnets (IDs) separadas por virgula (opcional)"
  type        = string
  default     = null
}

variable "platform_arn" {
  description = "Elastic Beanstalk Platform ARN"
  type        = string
}

variable "ec2_instance_type" {
  description = "Tipo de instância EC2"
  type        = string
  default     = "t3.micro"
}

variable "instance_profile_name" {
  description = "Nome do Instance Profile IAM para EC2"
  type        = string
}

variable "service_role_arn" {
  description = "ARN da Role do Serviço Elastic Beanstalk"
  type        = string
}

variable "security_group_id" {
  description = "ID do Security Group das instâncias"
  type        = string
}

variable "spring_datasource_url" {
  description = "URL de conexão JDBC do Spring Boot"
  type        = string
}

variable "spring_datasource_username" {
  description = "Usuário do banco de dados"
  type        = string
}

variable "spring_datasource_password" {
  description = "Senha do banco de dados"
  type        = string
  sensitive   = true
}

variable "aws_region" {
  description = "Região AWS"
  type        = string
}

variable "aws_ses_from_email" {
  description = "E-mail remetente verificado no SES"
  type        = string
}
