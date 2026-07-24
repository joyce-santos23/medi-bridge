variable "app_name" {
  description = "Nome da aplicação"
  type        = string
}

variable "environment" {
  description = "Nome do ambiente (ex: dev, prod, staging)"
  type        = string
}

variable "vpc_id" {
  description = "ID da VPC (opcional, utiliza a VPC default se não for informado)"
  type        = string
  default     = null
}

variable "app_port" {
  description = "Porta de execução da aplicação"
  type        = number
  default     = 8080
}

variable "db_port" {
  description = "Porta do banco de dados PostgreSQL"
  type        = number
  default     = 5432
}
