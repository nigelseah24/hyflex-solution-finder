# hyflex-solution-finder
Using the HyFlex framework, this project develops a hyper-heuristic to solve a UAV Zoo Feeding Problem (UZF)

### UAV Zoo Feeding Problem (UZF)
The UAV Zoo Feeding problem (UZF) is an NP-hard combinatorial optimisation problem which concerns a single UAV that needs to feed all animals in a zoo in the minimum amount of time. The animals in the zoo get particularly excited at feeding time and need to be fed by a UAV rather than a ranger for their welfare. During the feeding period, the zoo must close to visitors to ensure their safety. To increase revenue, the zoo needs to find a way of feeding all its animals in the least amount of time possible by finding an ordering of the animal enclosures for the feeding schedule.

### Problem Description
    Given a set of n animal enclosures, A, a relation, L, which maps all animal enclosures to physical locations by x and y coordinates, and a food preparation area, F, with location, (F_x,F_y ) where the UAV begins and ends its journey, the problem is to find an ordering of all animal enclosures which minimises the total time taken to feed all animals in the zoo as a function of the total distance the UAV needs to travel.

The output of the hyper-heuristic will be an image that connects all animal enclosures with lines, showing the route that the hyper-heuristic produces for the UAV to take, while at the same time calculating the objective function, which is the total distance travelled by the UAV. The lower the objective function, the better the hyper-heuristic as this is a minimisation problem.