# programq

A chatbot is a computer program which conducts a conversation via auditory or textual methods. 
Such programs are often designed to convincingly simulate how a human would behave as a conversational partner, 
thereby passing the Turing test. 

Chatbots are typically used in dialog systems for various practical purposes including customer service or information acquisition. 
Some chatterbots use sophisticated natural language processing systems, 
but many simpler systems scan for keywords within the input, then pull a reply with the most matching keywords, 
or the most similar wording pattern, from a database.

This chatbot combines ideas from Alexa, ALICE, and various other existing chatbots, as well as adds some new concepts altogether.
- the designer can add skills. A skill can get triggered based on input, in whatever way it chooses (keyword matching, regular expression matching, semantic vector embedding, ..)
- advanced AIML is implemented as a skill
- around 6000 AIML patterns are provided to help the chatbot reduce input
- near matching (based on Levenshtein distance) is implemented
- paragraph vector matching to be added soon

# AIML

AIML, or Artificial Intelligence Markup Language, is an XML dialect for creating natural language software agents.
The XML dialect called AIML was developed by Richard Wallace and a worldwide free software community between 1995 and 2002. 
AIML formed the basis for what was initially a highly extended Eliza called "A.L.I.C.E." ("Artificial Linguistic Internet Computer Entity"), 
which won the annual Loebner Prize Competition in Artificial Intelligence three times, and was also the Chatterbox Challenge Champion in 2004.

The XML files in this project can be considered an advanced extension on AIML. They support full regular expressions and can execute Javascript.
This allows the chatbot-designer much more freedom.
