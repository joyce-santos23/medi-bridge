resource "aws_db_instance" "postgres" {
  identifier             = "${var.app_name}-${var.environment}-db"
  engine                 = "postgres"
  engine_version         = "16.9"
  instance_class         = var.db_instance_class
  allocated_storage      = var.allocated_storage
  storage_type           = "gp3"
  db_name                = var.db_name
  username               = var.db_user
  password               = var.db_password
  port                   = var.db_port
  vpc_security_group_ids = [var.security_group_id]

  publicly_accessible = false
  skip_final_snapshot = true

  tags = {
    Name        = "${var.app_name}-${var.environment}-db"
    Environment = var.environment
  }
}
