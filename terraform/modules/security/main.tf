data "aws_vpc" "default" {
  count   = var.vpc_id == null ? 1 : 0
  default = true
}

locals {
  vpc_id = var.vpc_id != null ? var.vpc_id : data.aws_vpc.default[0].id
}

# Security Group para o Elastic Beanstalk
resource "aws_security_group" "beanstalk_sg" {
  name        = "${var.app_name}-${var.environment}-eb-sg"
  description = "Security Group para a aplicacao Elastic Beanstalk"
  vpc_id      = local.vpc_id

  ingress {
    description = "HTTP Traffic"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTPS Traffic"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Application Direct Port"
    from_port   = var.app_port
    to_port     = var.app_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "${var.app_name}-${var.environment}-eb-sg"
    Environment = var.environment
  }
}

# Security Group para o RDS PostgreSQL (aceita conexão apenas do Elastic Beanstalk)
resource "aws_security_group" "rds_sg" {
  name        = "${var.app_name}-${var.environment}-rds-sg"
  description = "Security Group para o RDS PostgreSQL - Apenas acesso vindo da aplicacao"
  vpc_id      = local.vpc_id

  ingress {
    description     = "PostgreSQL access from Elastic Beanstalk"
    from_port       = var.db_port
    to_port         = var.db_port
    protocol        = "tcp"
    security_groups = [aws_security_group.beanstalk_sg.id]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "${var.app_name}-${var.environment}-rds-sg"
    Environment = var.environment
  }
}
