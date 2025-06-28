#include <HTTPClient.h>
#include "APIController.h"
#include "Top2Items.h"

APIController::APIController() {
  // Add later method implementations here
}

bool APIController::begin() {
  // Add later method implementation here
  return true;
}

Top2Items APIController::getTasks() {
  // Make GET request
  // Use $top3 and $select=title to limit JSON size

  return Top2Items{};
}

