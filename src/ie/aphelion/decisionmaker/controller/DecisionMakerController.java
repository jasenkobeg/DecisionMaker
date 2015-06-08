package ie.aphelion.decisionmaker.controller;

import ie.aphelion.decisionmaker.Ballot;
import ie.aphelion.decisionmaker.BallotAnalysis;
import ie.aphelion.decisionmaker.BallotSearchResults;
import ie.aphelion.decisionmaker.BallotState;
import ie.aphelion.decisionmaker.Option;
import ie.aphelion.decisionmaker.SocialRanking;
import ie.aphelion.decisionmaker.Vote;
import ie.aphelion.decisionmaker.VoteData;
import ie.aphelion.decisionmaker.engine.BallotEngine;
import ie.aphelion.decisionmaker.service.BallotService;
import ie.aphelion.decisionmaker.service.VoterService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/v1")
public class DecisionMakerController extends AbstractController {

	/** The logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(DecisionMakerController.class);

	private static final int DEFAULT_PAGE_SIZE = 10;

	private static final String ID_FIELD_NAME = "id";
	private static final String NAME_FIELD_NAME = "name";
	private static final String STATUS_FIELD_NAME = "status";
	private static final String DESCRIPTION_FIELD_NAME = "description";
	private static final String NUMBER_OF_OPTIONS_FIELD_NAME = "numberofoptions";
	private static final String OPTION_FIELD_NAME_FORMAT_TEMPLATE = "option%d";

	private static final String JSON_VALUE = "value";
	private static final String JSON_KEY = "name";

	private static final long ZERO = 0;

	private static final String IGNORE = "ignore";

	private static final int FIRST_VOTE = 0;

	@Autowired
	private VoterService voterService;
	@Autowired
	private BallotService ballotService;

	/**
	 * Provides the REST API supported by this controller.
	 * 
	 * @param request
	 *            The HTTP request object.
	 * @return The API json response.
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<String> usage(final HttpServletRequest request) {
		LOG.info("Decision Maker API usage requested from {}",
				request.getRemoteAddr());
		final ArrayList<String> api = new ArrayList<String>();
		final Method[] methods = this.getClass().getDeclaredMethods();
		for (final Method method : methods) {
			final RequestMapping annotation = method
					.getAnnotation(RequestMapping.class);
			if (annotation != null)
				api.add(String.format("%s %s",
						Arrays.toString(annotation.method()),
						Arrays.toString(annotation.value())));
		}
		LOG.info("Decision Maker API usage is {}", api);
		return api;
	}

	/**
	 * Obtains all ballot ordered by identifier on the give page.
	 * 
	 * @return The JSON representation of a ballots.
	 */
	@ResponseBody
	@RequestMapping(value = "ballots/page/{page}/page-size/{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView getBallotsForPage(@PathVariable final String page,
			@PathVariable final String size, final HttpServletRequest request) {
		LOG.info(String
				.format("A client from %s is requesting the page [%s] with maximum of [%s] results",
						request.getRemoteAddr(), page, size));
		// Sanity check.
		if (page == null || page.trim().isEmpty()) {
			LOG.warn("Unable to obtain the page number.");
			throw new IllegalArgumentException(
					"Unable to obtain the page number.");
		}
		final int pageNumber = Integer.valueOf(page);
		if (pageNumber < 1) {
			LOG.warn("The given page number is less than 1.");
			throw new IllegalArgumentException(
					"The first page is numbers as 1.");
		}
		int pageSize = DEFAULT_PAGE_SIZE;
		if (size != null && Integer.valueOf(size) > 0) {
			pageSize = Integer.valueOf(size);
			LOG.debug("Setting page size to {}", pageSize);
		}
		final List<Ballot> ballots = this.ballotService.getBallots(pageSize,
				pageNumber);
		// See if the ballots have votes associated with them.
		for (final Ballot ballot : ballots) {
			ballot.setVotes(this.voterService.getVotesFor(ballot.getId()));
		}

		LOG.info("Found {} ballots", ballots == null ? 0 : ballots.size());

		final BallotSearchResults results = new BallotSearchResults(pageNumber,
				pageSize, ballots);
		return this.createModelAndView(results);
	}

	/**
	 * Creates a new ballot from a given JSON string.
	 * 
	 * @param ballotJSON
	 *            The JSON representation of a ballot.
	 */
	@Transactional
	@RequestMapping(value = "ballots", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void createBallot(
			final @RequestBody List<Map<String, String>> ballotJSON) {
		LOG.info("Creating ballot from {}", ballotJSON);

		final HashMap<String, String> values = new HashMap<String, String>();
		for (Map<String, String> formInput : ballotJSON) {
			values.put(formInput.get(JSON_KEY), formInput.get(JSON_VALUE));
		}

		final List<Option> options = new ArrayList<Option>();
		final int numnberOfOptions = Integer.valueOf(values
				.get(NUMBER_OF_OPTIONS_FIELD_NAME));
		for (int i = 0; i < numnberOfOptions; i++) {
			options.add(new Option(String.valueOf(values.get(String.format(
					OPTION_FIELD_NAME_FORMAT_TEMPLATE, i)))));
		}

		final String name = String.valueOf(values.get(NAME_FIELD_NAME));
		final String description = String.valueOf(values
				.get(DESCRIPTION_FIELD_NAME));

		final Ballot ballot = new Ballot(name, description);
		ballot.setOptions(options);

		final long id = this.ballotService.create(ballot);

		LOG.info("The ballot [{}] -> '{}' has been created", id, name);
	}

	/**
	 * Updates a new ballot from a given JSON string.
	 * 
	 * @param ballotJSON
	 *            The JSON representation of a ballot.
	 */
	@Transactional
	@RequestMapping(value = "ballots", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void updateBallot(
			final @RequestBody List<Map<String, String>> ballotJSON) {
		LOG.info("Updating ballot from {}", ballotJSON);

		final HashMap<String, String> values = new HashMap<String, String>();
		for (Map<String, String> formInput : ballotJSON) {
			values.put(formInput.get(JSON_KEY), formInput.get(JSON_VALUE));
		}
		
		final long id = Long.valueOf(values.get(ID_FIELD_NAME));
		// Close the ballot if the state is close.
		final BallotState state = BallotState.valueOf(String.valueOf(values.get(STATUS_FIELD_NAME)));
		if (BallotState.CLOSED.equals(state)) {
			LOG.warn("Closing ballot with id {}", id);
			this.ballotService.close(id);
			return;
		}

		final List<Option> options = new ArrayList<Option>();
		final int numberOfOptions = Integer.valueOf(values
				.get(NUMBER_OF_OPTIONS_FIELD_NAME));
		for (int i = 0; i < numberOfOptions; i++) {
			options.add(new Option(String.valueOf(values.get(String.format(
					OPTION_FIELD_NAME_FORMAT_TEMPLATE, i)))));
		}

		
		final String name = String.valueOf(values.get(NAME_FIELD_NAME));
		final String description = String.valueOf(values
				.get(DESCRIPTION_FIELD_NAME));
		

		// Do not add/remove options of the ballot if a ballot has votes associated
		// with it.
		final List<Vote> votes = this.voterService
				.getVotesFor(Long.valueOf(id));
		if (votes != null && !votes.isEmpty()) {
			final int numberOfCurrentOptions = votes.get(FIRST_VOTE)
					.getVoteData().size();
			if (numberOfCurrentOptions != numberOfOptions) {
				final String msg = String
						.format("Unable to update the ballot as there are %s votes associated with this ballot; id=%s",
								numberOfCurrentOptions, id);
				LOG.warn(msg);
				throw new RuntimeException(msg);
			}

		}

		final Ballot ballot = new Ballot(id, name, description);
		// Do not update the options if votes are associated with the ballot.
		// WARNING: if the options are update their ids will be updated, which 
		// in turn will cause problems with the results as the votes are
		// associated with the option identifiers.
		if (votes == null || votes.isEmpty()) {
			ballot.setOptions(options);
		}
		
		this.ballotService.update(ballot);
		LOG.info("The ballot [{}] -> '{}' has been updated", id, name);
	}

	/**
	 * Deletes a ballot with the given identifier. (Marks the ballot as deleted)
	 * 
	 * @param ballotJSON
	 *            The JSON representation of a ballot identifier.
	 */
	@Transactional
	@RequestMapping(value = "ballots", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteBallot(final @RequestBody long ballotId, final HttpServletRequest request) {
		LOG.info("Starting....");
		Enumeration<String> s = request.getParameterNames();
		while(s.hasMoreElements()) {
			String n = s.nextElement();
			LOG.info("AA {} BB {}", n, request.getParameter(n));	
		}
		
		LOG.info("Map size {}", request.getParameterMap().size());
		LOG.info("Map size {}", request.getContentLength());
		LOG.info("Map size {}", request.getContentType());
		LOG.info("Map size {}", request.getContextPath());
		
		try {
			LOG.info("IS {}", request.getInputStream().read(new byte[1024]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.info("ERROR {}", e.getMessage());
			e.printStackTrace();
		}
		Enumeration<String> s1 = request.getAttributeNames();
		
		while(s.hasMoreElements()) {
			String n1 = s1.nextElement();
			LOG.info("AA1 {} BB1 {}", n1, request.getParameter(n1));	
		}
		
		LOG.info("Deleting ballot with '{}' identifier", ballotId);

		// Sanity check
		if (ballotId <= ZERO) {
			LOG.error("Attempt to delete a balot with the '{}' identifier; the id seems invalid");
			throw new IllegalArgumentException(
					"Unable to delete a ballot; the identifier seems invalid.");
		}

		// TODO this really deletes the given ballot. However, we don't want to
		// lose the
		// data so we just mark a ballot as deleted.
		// this.ballotService.deleteBallot(ballotId);

		final Ballot ballot = this.ballotService.loadBallot(ballotId);
		ballot.setState(BallotState.DELETED);
		this.ballotService.update(ballot);

		LOG.info("The ballot with '{}' identifier has been deleted", ballotId);
	}

	/**
	 * Obtains a ballot for a given ballot identifier.
	 * 
	 * @param id
	 *            The identifier to look for.
	 * @param request
	 *            The request object.
	 * @return The JSON representation of a ballot.
	 */
	@ResponseBody
	@RequestMapping(value = "ballots/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView loadBallot(@PathVariable final String id,
			final HttpServletRequest request) {
		LOG.info(
				"A client from {} is requesting ballot associated with the [{}] identifier",
				request.getRemoteAddr(), id);
		// Sanity check.
		if (id == null || id.trim().isEmpty()) {
			LOG.warn("Unable to obtain ballot information. The ballot identifier is not specified");
			throw new IllegalArgumentException(
					"Unable to obtain ballot. Please specify a valid identifier.");
		}

		final Ballot ballot = this.ballotService.loadBallot(Long.valueOf(id));

		LOG.info("Loaded {}", ballot);
		return this.createModelAndView(ballot);
	}

	/**
	 * Obtains a ballot results information for a given ballot identifier.
	 * 
	 * @param id
	 *            The identifier to look for.
	 * @param request
	 *            The request object.
	 * @return The JSON representation of a ballot results.
	 */
	@ResponseBody
	@RequestMapping(value = "ballot-result/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView getBallotAnalysis(@PathVariable final String id,
			final HttpServletRequest request) {
		LOG.info(
				"A client from {} is requesting ballot results for [{}] identifier",
				request.getRemoteAddr(), id);

		// Sanity check.
		if (id == null || id.trim().isEmpty()) {
			LOG.warn("Unable to obtain ballot information. The ballot identifier is not specified");
			throw new IllegalArgumentException(
					"Unable to obtain ballot results. Please specify a valid identifier.");
		}

		final long ballotId = Long.valueOf(id);
		final List<Vote> votes = this.voterService.getVotesFor(ballotId);
		// Sanity check
		if (votes == null || votes.isEmpty()) {
			LOG.warn("No votes have been found for a ballot identified by {}",
					id);
		}

		final Ballot ballot = this.ballotService.loadBallot(ballotId);
		final int numberOfOptions = ballot.getOptions().size();
		final BallotEngine ballotEngine = this.voterService
				.getBallotEngine(votes, numberOfOptions);
		// Sanity check
		if (ballotEngine == null) {
			LOG.warn(
					"Unable to obtain information for a ballot identified by {}",
					id);
			return this.createModelAndView("Unable to process ballot results.");
		}
		
		try {
			final BallotAnalysis results = this.voterService
					.getBallotAnalysis(ballotEngine, numberOfOptions);
			results.setBallot(ballot);
			LOG.info("The ballot results are processed {}", results);
			return this.createModelAndView(results);
		} catch (final Throwable t) {
			LOG.error("Unable to process {}. {}", request, t.getMessage());
			throw new RuntimeException(t);
		}
	}

	/**
	 * Obtains all votes associated with the ballot.
	 * 
	 * @param id
	 *            The identifier to look for.
	 * @param request
	 *            The request object.
	 * @return The JSON representation of all votes associated with the ballot.
	 */
	@ResponseBody
	@RequestMapping(value = "ballot-votes/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView getBallotVotes(@PathVariable final String id,
			final HttpServletRequest request) {
		LOG.info(
				"A client from {} is requesting all votes associated with the ballot; id = [{}]",
				request.getRemoteAddr(), id);

		final List<Vote> votes = this.loadVotes(id);

		LOG.debug("The ballot {} has {}", id, votes);
		return this.createModelAndView(votes);
	}

	/**
	 * Allows casting of a vote as a JSON object.
	 * 
	 * @param voteJSON
	 *            The JSON representation of a vote.
	 */
	@Transactional
	@RequestMapping(value = "vote", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void processVote(
			final @RequestBody List<Map<String, String>> voteJSON) {
		LOG.info("Casting a vote {}", voteJSON);

		final HashMap<String, String> values = new HashMap<String, String>();

		for (Map<String, String> formInput : voteJSON) {
			values.put(formInput.get(JSON_KEY), formInput.get(JSON_VALUE));
		}

		final Vote vote = new Vote();
		final List<VoteData> votes = new ArrayList<VoteData>();
		for (final String key : values.keySet()) {
			if (!key.equalsIgnoreCase(ID_FIELD_NAME) && !key.startsWith(IGNORE)) {
				final VoteData data = new VoteData();
				data.setOptionId(Long.valueOf(key));
				data.setValue(Integer.valueOf(values.get(key)));
				votes.add(data);
			}
		}

		vote.setBallotId(Long.valueOf(values.get(ID_FIELD_NAME)));
		vote.setVoteData(votes);

		final long id = this.voterService.cast(vote);
		LOG.info("The vote [{}] for the [{}] ballot has been registered", id,
				values.get(ID_FIELD_NAME));
	}
	
	/**
	 * Obtains social choice/ranking for the ballot.
	 * 
	 * @param id
	 *            The identifier to look for.
	 * @param request
	 *            The request object.
	 * @return The JSON representation of all votes associated with the ballot.
	 */
	@ResponseBody
	@RequestMapping(value = "ballots-social-ranking/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView getBallotSocialRanking(@PathVariable final String id,
			final HttpServletRequest request) {
		LOG.info(
				"A client from {} is requesting social ranking the ballot; id = [{}]",
				request.getRemoteAddr(), id);

		final List<Vote> votes = this.loadVotes(id);
		if (votes == null || votes.isEmpty()) { 
			//throw new RuntimeException("HANDLE THIS CORRECTLY, ONCE YOU KONW WHAT'S BEING RETURNED");
			LOG.warn("Unable to load votes for social ranking associated with {} identifier", id);
			final SocialRanking empty = new SocialRanking();
			final ArrayList<SocialRanking> list = new ArrayList<SocialRanking>();
			list.add(empty);
			return this.createModelAndView(list);
		}

		final ArrayList<Vote> socialVotes = new ArrayList<Vote>();
		for (final Vote vote : votes) {
			final List<VoteData> dataList = vote.getVoteData();
			final int maxNumberOfPoints = dataList.size();
			vote.setVoteData(this.calculatePoints(dataList, maxNumberOfPoints));
			socialVotes.add(vote);
		}

		final int totalNumberOfVotes = socialVotes.size();
		final HashMap<Integer, SocialRanking> totals = new HashMap<Integer, SocialRanking>();
		for (final Vote vote : socialVotes) {
			int index = 0;
			int numberOfOptions = vote.getVoteData().size();
			for (final VoteData data : vote.getVoteData()) {
				index++;
				LOG.debug(String.format("voteid:%s, optionid:%s, value:%s, points:%s", data.getVoteId(), data.getOptionId(), data.getValue(), data.getPoints()));
				int points = data.getPoints();
				if (points < 0) // ensure that the negative values don't reduce the total
					points = 0;
				SocialRanking socialRanking = totals.get(index);
				if (socialRanking == null) // first time
					socialRanking = new SocialRanking();
				if (socialRanking.getOption() == null || socialRanking.getOption().trim().isEmpty()) {
					final Option loadedOption = this.ballotService.getOption(data.getOptionId());
					LOG.debug("loaded {}", loadedOption);
					socialRanking.setOption(loadedOption.getName());
				}
				int currentTotal = socialRanking.getPointsReceived();
				// sum it up.
				currentTotal = currentTotal + points;
				double denominator = (numberOfOptions * totalNumberOfVotes);
				double coefficient  = currentTotal / denominator;
				socialRanking.setPointsReceived(currentTotal);
				socialRanking.setConsensusCoefficient(String.valueOf(coefficient));
				totals.put(index, socialRanking);
			}
		}
		LOG.debug("Social ranking points received per option {} ", totals.values());
		return this.createModelAndView(totals.values());
	}
	
	/**
	 * Calculates number of points per vote.
	 * @param dataList The vote details.
	 * @param maxPoints The max amount of points available.
	 * @return The list of data with points per vote.
	 */
	private List<VoteData> calculatePoints(final List<VoteData> dataList, int maxPoints) {
		LOG.debug("Calculating points for {}", dataList);
		
		int pointsPerVote = this.calculateVotePoint(dataList, maxPoints); 

		final ArrayList<VoteData> socialVoteData = new ArrayList<VoteData>();
		for (final VoteData data : dataList) {
			int val = data.getValue();
			data.setPoints( pointsPerVote - (val == ZERO ? maxPoints : val)); 
			socialVoteData.add(data);
		}
		
		LOG.debug("The vote data with points {}", socialVoteData);
		return socialVoteData;
	}

	/**
	 * Calculate vote point per vote.
	 * @param dataList The vote details.
	 * @param maxPoints The maximum amount of points available per ballot.
	 * @return The point per vote.
	 */
	private int calculateVotePoint(final List<VoteData> dataList, int maxPoints) {
		LOG.debug("Calculating vote point for {}", dataList);
		
		int max = maxPoints + 1;
		int noVoteCounter = 0;
		for (final VoteData data : dataList) {
			if (data.getValue() == ZERO) 
				noVoteCounter++;
		}
		
		int points = max - noVoteCounter;
		
		LOG.debug("The vote scored {} out of {}", points, maxPoints);
		return points;
	}
	
	/**
	 * Load the votes for the given ballot identifier.
	 * 
	 * @param id
	 *            The ballot identifier
	 * @return The list of votes associated with a ballot or an empty list.
	 */
	private List<Vote> loadVotes(String id) {
		// Sanity check.
		if (id == null || id.trim().isEmpty()) {
			LOG.warn("Unable to obtain ballot information. The ballot identifier is not specified");
			throw new IllegalArgumentException(
					"Unable to obtain ballot results. Please specify a valid identifier.");
		}

		final List<Vote> votes = this.voterService
				.getVotesFor(Long.valueOf(id));
		// Sanity check
		if (votes == null || votes.isEmpty()) {
			final String message = String.format(
					"No votes have been found for a ballot identified by %s",
					id);
			// LOG.error(message);
			// throw new RuntimeException(message);
			LOG.warn(message);
			return new ArrayList<Vote>();
		}
		return votes;
	}

}