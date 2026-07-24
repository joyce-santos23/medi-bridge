# Assume Role Policy para as instâncias EC2 do Elastic Beanstalk
data "aws_iam_policy_document" "ec2_assume_role" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

# Role do EC2 para a instância do Beanstalk
resource "aws_iam_role" "eb_ec2_role" {
  name               = "${var.app_name}-${var.environment}-eb-ec2-role"
  assume_role_policy = data.aws_iam_policy_document.ec2_assume_role.json

  tags = {
    Name        = "${var.app_name}-${var.environment}-eb-ec2-role"
    Environment = var.environment
  }
}

# Attachments para a Role do EC2
resource "aws_iam_role_policy_attachment" "eb_web_tier" {
  role       = aws_iam_role.eb_ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier"
}

resource "aws_iam_role_policy_attachment" "eb_multicontainer_docker" {
  role       = aws_iam_role.eb_ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkMulticontainerDocker"
}

resource "aws_iam_role_policy_attachment" "eb_worker_tier" {
  role       = aws_iam_role.eb_ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkWorkerTier"
}

# Política customizada para permissão de envio de e-mails via SES
data "aws_iam_policy_document" "ses_policy_doc" {
  statement {
    effect = "Allow"
    actions = [
      "ses:SendEmail",
      "ses:SendRawEmail",
      "ses:GetSendQuota",
      "ses:GetSendStatistics"
    ]
    resources = ["*"]
  }
}

resource "aws_iam_policy" "ses_policy" {
  name        = "${var.app_name}-${var.environment}-ses-policy"
  description = "Permite a aplicacao enviar e-mails via Amazon SES"
  policy      = data.aws_iam_policy_document.ses_policy_doc.json
}

resource "aws_iam_role_policy_attachment" "eb_ses_attach" {
  role       = aws_iam_role.eb_ec2_role.name
  policy_arn = aws_iam_policy.ses_policy.arn
}

# Instance Profile para o Elastic Beanstalk
resource "aws_iam_instance_profile" "eb_ec2_profile" {
  name = "${var.app_name}-${var.environment}-eb-ec2-profile"
  role = aws_iam_role.eb_ec2_role.name
}

# Assume Role Policy para o Serviço do Elastic Beanstalk
data "aws_iam_policy_document" "eb_service_assume_role" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["elasticbeanstalk.amazonaws.com"]
    }
  }
}

# Role do Serviço Elastic Beanstalk
resource "aws_iam_role" "eb_service_role" {
  name               = "${var.app_name}-${var.environment}-eb-service-role"
  assume_role_policy = data.aws_iam_policy_document.eb_service_assume_role.json

  tags = {
    Name        = "${var.app_name}-${var.environment}-eb-service-role"
    Environment = var.environment
  }
}

resource "aws_iam_role_policy_attachment" "eb_enhanced_health" {
  role       = aws_iam_role.eb_service_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSElasticBeanstalkEnhancedHealth"
}


