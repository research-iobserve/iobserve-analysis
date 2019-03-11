# Evaluation Logs

Events: d1...d5 = Deployment events for node 1 to 5
        ud1...ud5 = Undeployment events for node 1 to 5

- migration-A: d1,ud1,d2,ud2,d3,ud3,d4,ud4,d5
  Migration with short time of undeployment
- migration-B: d1,d2,ud1,d3,ud2,d4,ud3,d5,ud4
  Overlapping migration
- replication: d1,d2,d3,d4,d5
- dereplication-A: d1,d2,d3,d4,d5,ud5,ud4,ud3,ud2
  Replicates first then dereplicates
- dereplication-B: ud5,ud4,ud3,ud2
  Assumes a model which already contains replicated components


