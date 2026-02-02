{{- define "java-demo.name" -}}
java-demo
{{- end -}}

{{- define "java-demo.labels" -}}
app.kubernetes.io/name: {{ include "java-demo.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}
