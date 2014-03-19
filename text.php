<?php

//Including the API PHP library
require_once JPATH_COMPONENT . '/assets/php/vendor/autoload.php';

class GithubHelper {

    /**
     * Method for getting the Github access token configured by the user.
     * @return mixed returns the access token if it exists, null otherwise
     */
    private static function getAccessToken($jcc_token) {

        if ($jcc_token) {
            jimport('joomla.application.component.helper');
            return JComponentHelper::getParams('com_combuilder')->get('github_account_token_jcc');
        } else {
            $dispatcher = JDispatcher::getInstance();
            JPluginHelper::importPlugin('user');
            $user = JFactory::getUser();
            $dispatcher->trigger('onContentPrepareData', array('com_users.profile', $user));

            return empty($user->nwd['github_access_token']) ? null : $user->nwd['github_access_token'];
        }
    }

    /**
     * Loads all the repository from a particular user.
     * @param $username The username of the repositories owner. If $username contains null, gets all the repositories from the current logged user.
     * @return array An array with all the repositories information.
     */
    public static function getRepositories($username = null) {
        //Create a github client
        $github = self::createClientAuthenticate();

        //If the username was not specified, let's load the repositories from the current logged user.
        if (empty($username)) {
            $repos = $github->api('current_user')->repositories();
        } else {
            $repos = $github->api('current_user')->repositories();
        }

        return $repos;
    }

    /**
     * Get all the pull requests from a repository
     * @param type $repository
     * @param type $username
     * @return type
     */
    public static function getPullRequests($repository, $username = null) {
        //Create a github client
        $github = self::createClientAuthenticate();

        //If the username is null, let's get the current logged in user username
        if (empty($username)) {
            $username = self::getUsernameOfCurrentUserLogged($github);
        }
        return $github->api('pull_request')->all($username, $repository);
    }

    public static function createFile($repository, $commit_message, $path_file, $branch = null) {
        //Create a github client
        $github = self::createClientAuthenticate();

        //Getting the username of the current logged user
        $username = self::getUsernameOfCurrentUserLogged($github);

        //Getting the contents of the file
        $file_content = file_get_contents($path_file);
        $response = $github->api('repo')->contents()->create($username, $repository, 'text.php', $file_content, $commit_message, $branch);
        echo '<pre class="debug"><small>' . __file__ . ':' . __line__ . "</small>\n\$response = " . print_r($response, true) . "\n</pre>";
        exit;
    }

    public static function updateFile($repository, $commit_message, $path_file, $jcc_user = false, $branch = null) {
        //Create a github client
        $github = self::createClientAuthenticate($jcc_user);

        //Getting the username of the current logged user
        $username = self::getUsernameOfCurrentUserLogged($github);

        //Getting the contents of the file
        $file_content = file_get_contents(__FILE__);
        $file_info = self::getFileInformation($repository, 'text.php', $jcc_user, $branch);
        $response = $github->api('repo')->contents()->update($username, $repository, 'text.php', $file_content, $commit_message, $file_info['sha'], $branch);
        echo '<pre class="debug"><small>' . __file__ . ':' . __line__ . "</small>\n\$response = " . print_r($response, true) . "\n</pre>";
        exit;
    }

    public static function getFileInformation($repository, $path_file, $jcc_user = false, $branch = null) {
        //Create a github client
        $github = self::createClientAuthenticate($jcc_user);

        //Getting the username of the current logged user
        $username = self::getUsernameOfCurrentUserLogged($github);

        //Getting the contents of the file
        $response = $github->api('repo')->contents()->show($username, $repository, $path_file, $branch);

        return $response;
    }

    public static function isFileCommited($repository, $path_file, $branch = null) {
        //Create a github client
        $github = self::createClientAuthenticate();

        //Getting the username of the current logged user
        $username = self::getUsernameOfCurrentUserLogged($github);

        //Getting the contents of the file
        $response = $github->api('repo')->contents()->show($username, $repository);

        echo '<pre class="debug"><small>' . __file__ . ':' . __line__ . "</small>\n\$response = " . print_r($response, true) . "\n</pre>";
        exit;
    }

    /**
     * Create a fork from a repository
     * @param string owner username
     * @param string $repository Repository name
     * @param boolean $jcc_user
     * @return array New repository data.
     */
    public static function forkRepository($username, $repository, $jcc_user) {
        //Create a github client
        $github = self::createClientAuthenticate($jcc_user);

        //Forking the repository
        $repository = $github->api('repo')->forks()->create($username, $repository);
        return $repository;
    }

    /**
     * Create a Github client authenticate by the OAuth token
     * @return \Github\Client Github client object
     */
    private static function createClientAuthenticate($jcc_account) {
        $github = new \Github\Client();
        $github->authenticate(self:: getAccessToken($jcc_account), Github\Client::AUTH_HTTP_TOKEN);

        return $github;
    }

    /**
     * Get the username of the current user logged.
     * @param \Github\Client $github Github client object
     * @return string The username
     */
    private static function getUsernameOfCurrentUserLogged($github) {
        $user_data = $github->api('current_user')->show();
        return $user_data['login'];
    }

}
