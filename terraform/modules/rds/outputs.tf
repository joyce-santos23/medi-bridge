output "db_endpoint" {
  description = "Endpoint de conexão com o banco de dados (host:port)"
  value       = aws_db_instance.postgres.endpoint
}

output "db_address" {
  description = "Endereço host do banco de dados"
  value       = aws_db_instance.postgres.address
}

output "db_port" {
  description = "Porta do banco de dados"
  value       = aws_db_instance.postgres.port
}

output "db_name" {
  description = "Nome do banco de dados"
  value       = aws_db_instance.postgres.db_name
}

output "db_username" {
  description = "Usuário master do banco de dados"
  value       = aws_db_instance.postgres.username
}
