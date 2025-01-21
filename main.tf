/*
provider "aws" {
    region = "eu-west-2"
}

resource "aws_instance" "example" {
    ami = "ami-091f18e98bc129c4e"
    instance_type = "t2.micro"

    tags = {
        Name = "terraform-example"
    }
}
*/

# NEW CONFIGS
 provider "aws" {
    region = "eu-west-2"
}

# Create or import the key pair
resource "aws_key_pair" "terraform_key" {
    key_name   = "terraform-key"
    public_key = file("~/.ssh/terraform-key.pub") # Path to your public SSH key
}

# Security group to allow SSH access
resource "aws_security_group" "ssh_access" {
    name        = "allow_ssh"
    description = "Allow SSH access"

    ingress {
        from_port   = 22
        to_port     = 22
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"] # Allow SSH from anywhere (use caution, restrict CIDR if possible)
    }

    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1" # Allow all outbound traffic
        cidr_blocks = ["0.0.0.0/0"]
    }
}

# Launch the EC2 instance with the key pair and security group
resource "aws_instance" "example" {
    ami           = "ami-091f18e98bc129c4e"
    instance_type = "t2.micro"

    key_name = aws_key_pair.terraform_key.key_name # Attach the key pair

    vpc_security_group_ids = [aws_security_group.ssh_access.id] # Attach the security group

    tags = {
        Name = "terraform-example"
    }
}
