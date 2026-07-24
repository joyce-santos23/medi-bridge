module "security" {
  source      = "./modules/security"
  app_name    = var.app_name
  environment = var.environment
  db_port     = var.db_port
}

module "iam" {
  source      = "./modules/iam"
  app_name    = var.app_name
  environment = var.environment
}

module "rds" {
  source            = "./modules/rds"
  app_name          = var.app_name
  environment       = var.environment
  db_name           = var.db_name
  db_user           = var.db_user
  db_password       = var.db_password
  db_instance_class = var.db_instance_class
  db_port           = var.db_port
  security_group_id = module.security.rds_security_group_id
}

module "beanstalk" {
  source                = "./modules/beanstalk"
  app_name              = var.app_name
  environment           = var.environment
  vpc_id                = module.security.vpc_id
  platform_arn          = var.platform_arn
  ec2_instance_type     = var.ec2_instance_type
  instance_profile_name = module.iam.ec2_instance_profile_name
  service_role_arn      = module.iam.service_role_arn
  security_group_id     = module.security.beanstalk_security_group_id

  # Variáveis de Ambiente injetadas na aplicação Spring Boot
  spring_datasource_url      = "jdbc:postgresql://${module.rds.db_endpoint}/${var.db_name}"
  spring_datasource_username = var.db_user
  spring_datasource_password = var.db_password
  aws_region                 = var.aws_region
  aws_ses_from_email         = var.aws_ses_from_email
}
