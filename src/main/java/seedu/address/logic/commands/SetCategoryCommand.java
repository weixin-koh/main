package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CUISINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCASION;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.model.restaurant.categories.Cuisine;

/**
 * Sets the cuisine of an existing restaurant in the food diary.
 */
public class SetCategoryCommand extends Command {

    public static final String COMMAND_WORD = "setCategory";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets categories of the restaurant identified by the index number used in the displayed "
            + "restaurant list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_CUISINE + "CUISINE] "
            + "[" + PREFIX_OCCASION + "OCCASION]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CUISINE + "Fine Dining "
            + PREFIX_OCCASION + "Wedding";

    public static final String MESSAGE_SET_CUISINE_SUCCESS = "Cuisine Set for Restaurant: %1$s";

    private final Index index;
    private final Cuisine cuisine;

    public SetCategoryCommand(Index index, Cuisine cuisine) {
        requireNonNull(index);
        requireNonNull(cuisine);

        this.index = index;
        this.cuisine = cuisine;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToAddCuisine = lastShownList.get(index.getZeroBased());
        Restaurant restaurantWithCuisineAdded = new Restaurant(restaurantToAddCuisine, this.cuisine);

        model.setRestaurant(restaurantToAddCuisine, restaurantWithCuisineAdded);
        model.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);
        model.commitFoodDiary();
        return new CommandResult(String.format(MESSAGE_SET_CUISINE_SUCCESS, restaurantWithCuisineAdded));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SetCategoryCommand)) {
            return false;
        }

        // state check
        SetCategoryCommand e = (SetCategoryCommand) other;
        return index.equals(e.index)
                && cuisine.equals(e.cuisine);
    }
}
