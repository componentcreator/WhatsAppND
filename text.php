<?php

// No direct access
defined('_JEXEC') or die('Restricted access');

//sessions
jimport('joomla.session.session');

require_once JPATH_COMPONENT_ADMINISTRATOR . '/helpers/combuilder.php';
require_once JPATH_COMPONENT . '/helpers/github.php';

GithubHelper::createFile('WhatsAppND', 'Testing API', __FILE__);

CombuilderHelper::updateUserInSession();
CombuilderHelper::setItemids(); //Set the items ids of the menus ('Itemid' param)
//Load classes
JLoader::registerPrefix('Combuilder', JPATH_COMPONENT);

//Load plugins
JPluginHelper::importPlugin('Combuilder');

//application
$app = JFactory::getApplication();

//To prevent rewriting a lot of controller code we are here mimicking the behaviour from Joomla 2.5
// task=controller.method
$task = $app->input->get('task', '');
if (!empty($task) && count(explode('.', $task)) > 1) {
    $task_data = explode('.', $task);

    $controller = $task_data[0];
    $method = $task_data[1];
} else {
    // Require specific controller if requested
    $controller = $app->input->get('controller', 'default');
    $method = 'execute';
}

// Create the controller
$classname = 'CombuilderControllers' . ucwords($controller);
$controller = new $classname();

// Perform the Request task
$controller->$method();
