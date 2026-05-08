pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'ap-south-1'
        AMI_ID = 'ami-07a00cf47dbbc844c'   // ✅ fixed
        INSTANCE_TYPE = 't3.micro'
        KEY_NAME = 'new-aws-key'
        SECURITY_GROUP = 'sg-03ca2d62d33cd42a2'
        SUBNET_ID = 'subnet-01da04c48d3714008'
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
                        --count 2 \
                        --instance-type $INSTANCE_TYPE \
                        --key-name $KEY_NAME \
                        --security-group-ids $SECURITY_GROUP \
                        --subnet-id $SUBNET_ID \
                        --associate-public-ip-address \
                        --query 'Instances[0].InstanceId' \
                        --output text
                    '''
                }
            }
        }
    }
}
