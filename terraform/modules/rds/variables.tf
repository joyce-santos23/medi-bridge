variable "app_name" {
  description = "Nome da aplicação"
  type        = string
}

variable "environment" {
  description = "Nome do ambiente"
  type        = string
}

variable "db_name" {
  description = "Nome do banco de dados PostgreSQL"
  type        = string
  default     = "medibridge"
}

variable "db_user" {
  description = "Usuário do banco de dados PostgreSQL"
  type        = string
  default     = "medibridge"
}

variable "db_password" {
  description = "Senha do banco de dados PostgreSQL"
  type        = string
  sensitive   = true
}

variable "db_instance_class" {
  description = "Classe de instância do RDS"
  type        = string
  default     = "db.t3.micro"
}

variable "db_port" {
  description = "Porta do PostgreSQL"
  type        = number
  default     = 5432
}

variable "security_group_id" {
  description = "ID do Security Group permitido no RDS"
  type        = string
}

variable "allocated_storage" {
  description = "Tamanho do armazenamento em GB"
  type        = number
  default     = 20
}
