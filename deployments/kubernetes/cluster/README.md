# PodPresets

- requires the `PodPreset` admission controller plugin

```shell
minikube start --extra-config=apiserver.Admission.PluginNames=\
  NamespaceLifecycle,LimitRanger,ServiceAccount,PersistentVolumeLabel,\
  DefaultStorageClass,ResourceQuota,DefaultTolerationSeconds,PodPreset
```