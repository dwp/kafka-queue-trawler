resource "aws_ecr_repository" "kafka-queue-trawler" {
  name = "kafka-queue-trawler"
  tags = merge(
    local.common_tags,
    { DockerHub : "dwpdigital/kafka-queue-trawler" }
  )
}

resource "aws_ecr_repository_policy" "kafka-queue-trawler" {
  repository = aws_ecr_repository.kafka-queue-trawler.name
  policy     = data.terraform_remote_state.management.outputs.ecr_iam_policy_document
}

output "ecr_example_url" {
  value = aws_ecr_repository.kafka-queue-trawler.repository_url
}
