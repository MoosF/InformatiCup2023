package simulation;

import java.util.Collection;

/**
 * {@link Coupled} represents a coupling of a receiver and some givers. {@link SimulatableObject}s
 * are coupled, if the output of the givers are connected to the input of the receiver.
 *
 * @author Yannick Kraml
 */
public record Coupled(SimulatableObject receiver, Collection<SimulatableObject> givers) {

}
