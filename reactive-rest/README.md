# Reactive CQRS REST 

## Eventual consistency

In this demo we will try to solve eventual consistency in CQRS system.
What we want to achieve is that our REST API feels native and on sending a command it returns updated projection right away, instead of listening for updates on a different endpoint.

There are several issues that needs to be address:

1. We need to subscribe to updates same time as we are sending the command, that's the only way to be sure we will not miss any updates. Sending commands first and then subscribing for updates will result with race condition! 
2. We need to read our own writes, multiple updates/events could be dispatch in same time, we can't quarantine order and which one will arrive first. Without some kind of correlation we will easily get into trouble and get someone else's update. The Safest way to go is to introduce unique command id for each command. We can attach this data to event meta-data, and once projection is materialized we can track which command is responsible for this update. As alternative, you can use some kind of HTTP request id or even user id of the user who is making the action. API would feel nicer but command id is still safest way to go.

Axon provided subscription queries which will emit updates already have Reactive API, so all we need to do, in order to get started, is to add simple reactive wrapper around command gateway.
As we are using Reactive API, this operation needs to be non-blocking and resilient.
