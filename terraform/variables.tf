variable "aws_region" {
  description = "Região AWS para implantação dos recursos"
  type        = string
  default     = "us-east-1"
}

variable "app_name" {
  description = "Nome da aplicação"
  type        = string
  default     = "medi-bridge"
}

variable "environment" {
  description = "Ambiente de implantação (ex: dev, prod)"
  type        = string
  default     = "prod"
}

variable "db_name" {
  description = "Nome do banco de dados PostgreSQL no RDS"
  type        = string
  default     = "medibridge"
}

variable "db_user" {
  description = "Usuário mestre do banco de dados PostgreSQL"
  type        = string
  default     = "medibridge"
}

variable "db_password" {
  description = "Senha mestre do banco de dados PostgreSQL"
  type        = string
  sensitive   = true
}

variable "db_instance_class" {
  description = "Tipo/Classe da instância do RDS PostgreSQL"
  type        = string
  default     = "db.t3.micro"
}

variable "db_port" {
  description = "Porta do banco de dados PostgreSQL"
  type        = number
  default     = 5432
}

variable "solution_stack_name" {
  description = "Plataforma do Elastic Beanstalk (Docker)"
  type        = string
  default     = "64bit Amazon Linux 2023 v4.4.1 running Docker"
}

variable "platform_arn" {
  description = "Platform ARN do Elastic Beanstalk"
  type        = string
}

variable "ec2_instance_type" {
  description = "Tipo da instância EC2 do Beanstalk"
  type        = string
  default     = "t3.micro"
}

variable "aws_ses_from_email" {
  description = "E-mail remetente pré-verificado no AWS SES"
  type        = string
  default     = "abdalla.joyce@gmail.com"
}
