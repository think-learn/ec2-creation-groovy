pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'ap-south-1'   // change if needed
        AMI_ID = 'ami-05d2d839d4f73aafb-'    // Amazon Linux (example)
        INSTANCE_TYPE = 't2.micro'
        KEY_NAME = 'new-aws-key'
        SECURITY_GROUP = 'sg-03600a51d96a85b44'
        SUBNET_ID = 'subnet-069f9b5757ce0ba4d'
    }

    stages {
        stage('Create EC2 Instance') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-jenkins-creds'
                ]]) {
                    sh '''
                    aws ec2 run-instances \
                        --image-id $AMI_ID \
                        --count 1 \
                        --instance-type $INSTANCE_TYPE \
                        --key-name $KEY_NAME \
                        --security-group-ids $SECURITY_GROUP \
                        --subnet-id $SUBNET_ID \
                        --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=Jenkins-EC2}]'
                    '''
                }
            }
        }
    }
}
