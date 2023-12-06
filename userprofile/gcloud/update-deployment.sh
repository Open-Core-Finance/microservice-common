# gcloud deployment-manager deployments update userprofile --config deploy.yaml
kubectl apply -f deploy.yaml

kubectl rollout restart deploy userprofile