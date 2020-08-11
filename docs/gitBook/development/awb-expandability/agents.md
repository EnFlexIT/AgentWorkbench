# Agents

Agents are at the heart of every Agent.Workbench project. Consequently, if you want to work with Agent.Workbench you will deal with agents at some point of time. The following text covers the topics:

* What is an agent?
* What are Mulit-Agent-Systems \(MAS\)?
* About agents in Agent.Workbench

## What is an agent?

There have been several definitions of the term "agent" over the years. A widely accepted one comes from M. Wooldridge and Jennings \(1995\) \[[1](agents.md#further-readings)\]:

“An agent is a computer system that is situated in some environment, and that is capable of autonomous action in this environment in order to meet its delegated objectives”

To put it in simple words, an agent is a autonomous software component that performs various actions in order to accomplish certain tasks that are assigned to him. The following quote gives a good summary of the characteristics and potentials of agents:

"...an agent is _autonomous_, because it operates without the direct intervention of humans or others and has control over its actions and internal state. An agent is _social_, because it cooperates with humans or other agents in order to achieve its tasks. An agent is _reactive_, because it perceives its environment and responds in a timely fashion to changes that occur in the environment. And an agent is _proactive_, because it does not simply act in response to its environment but is able to exhibit goal-directed behavior by taking initiative. Moreover, if necessary an agent can be _mobile_, with the ability to travel between different nodes in a computer network." \[[2](agents.md#further-readings)\]

To illustrate this concept, think of agents as football players during a match. Each football player represents an agent. Although the football player is free to go wherever he likes \(_autonomous_\) inside the boundaries of the football field \(_environment_\), he has the game plan for the match on his mind and plays accordingly. Each football player can communicate with his teammates to corrdinate the next move \(_social_\). A football player always has to perceive his environment \(where are my opponents? How much time is left on the clock?\) and react to changes \(_reactive_\). But if a football player is in possession of the ball, he tries to actively enforce his goals \(_proactive_\).

## What are Mulit-Agent-Systems \(MAS\)?

Multiple agents acting in the same environment form a multi-agent systems \(MAS\). These agents can have common, but also conflicting goals. To continue the football match analogy, all 22 players on the football field create a complex system \(_MAS_\), with each football player \(_agent_\) having common goals with his teammates and conflicting goals with his opponents.

## About agents in Agent.Workbench

Agent.Workbench builds upon the Java Agent DEvelopment framework JADE. JADE provides the basic infrastructure for operating MAS, and comes with a class library as a foundation for building your own MAS. All agent-systems created with Agent.Workbench are executed on the JADE platform.

In Agent.Workbench, MAS are handled as projects that can be configured via the application. In order to create your own agents for an Agent.Workbench project, all you have to do is extending the Agent superclass provided by the JADE library. Detailed instructions on how to create an agent and how to execute it from inside Agent.Workbench can be found [in this tutorial](../basic-steps/create-a-project-plugin.md).

## Further Readings

\[1\] Michael Wooldridge, Nicholas R. Jennings \(1995\) "Intelligent agents: Theory and Practice"

\[2\] Fabio Luigi Bellifemine, Giovanni Caire, Dominic Greenwood \(2007\) "Developing Multi-Agent Systems with JADE". The book provides a comprehensive explanation of the features provided by the Java Agent Development framework JADE.  


