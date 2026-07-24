# Busca automatica das subnets da VPC caso nao tenham sido informadas explicitamente
data "aws_subnets" "vpc_subnets" {
  count = var.subnets == null ? 1 : 0
  filter {
    name   = "vpc-id"
    values = [var.vpc_id]
  }
}

# Detalhes das subnets para checar a Availability Zone (ex: us-east-1e nao suporta t3.micro)
data "aws_subnet" "subnet_details" {
  for_each = var.subnets == null ? toset(data.aws_subnets.vpc_subnets[0].ids) : []
  id       = each.value
}

locals {
  # Filtra apenas subnets em Availability Zones compativeis com t3.micro (exclui us-east-1e)
  compatible_subnet_ids = var.subnets != null ? var.subnets : join(",", [
    for s in data.aws_subnet.subnet_details : s.id if s.availability_zone != "us-east-1e"
  ])
}

# Aplicacao Elastic Beanstalk
resource "aws_elastic_beanstalk_application" "app" {
  name        = var.app_name
  description = "Elastic Beanstalk Application para ${var.app_name}"

  tags = {
    Name        = var.app_name
    Environment = var.environment
  }
}

# Ambiente Elastic Beanstalk
resource "aws_elastic_beanstalk_environment" "env" {
  name         = "${var.app_name}-${var.environment}-env"
  application  = aws_elastic_beanstalk_application.app.name
  platform_arn = var.platform_arn

  # --- CONFIGURACAO DE VPC ---
  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = var.vpc_id
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value     = local.compatible_subnet_ids
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "AssociatePublicIpAddress"
    value     = "true"
  }

  # --- CONFIGURACAO DE INSTANCIA EC2 E LAUNCH TEMPLATE ---
  setting {
    namespace = "aws:ec2:instances"
    name      = "InstanceTypes"
    value     = var.ec2_instance_type
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = var.instance_profile_name
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "InstanceType"
    value     = var.ec2_instance_type
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "SecurityGroups"
    value     = var.security_group_id
  }

  # Service Role do Elastic Beanstalk
  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "ServiceRole"
    value     = var.service_role_arn
  }

  # CloudWatch Logs integration
  setting {
    namespace = "aws:elasticbeanstalk:cloudwatch:logs"
    name      = "StreamLogs"
    value     = "true"
  }

  setting {
    namespace = "aws:elasticbeanstalk:cloudwatch:logs"
    name      = "DeleteOnTerminate"
    value     = "false"
  }

  setting {
    namespace = "aws:elasticbeanstalk:cloudwatch:logs"
    name      = "RetentionInDays"
    value     = "7"
  }

  # Variáveis de ambiente para o Spring Boot
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "SPRING_DATASOURCE_URL"
    value     = var.spring_datasource_url
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "SPRING_DATASOURCE_USERNAME"
    value     = var.spring_datasource_username
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "SPRING_DATASOURCE_PASSWORD"
    value     = var.spring_datasource_password
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "AWS_REGION"
    value     = var.aws_region
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "AWS_SES_FROM_EMAIL"
    value     = var.aws_ses_from_email
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "PORT"
    value     = "8080"
  }

  tags = {
    Name        = "${var.app_name}-${var.environment}-env"
    Environment = var.environment
  }
}
