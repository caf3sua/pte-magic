package com.vmcomms.ptemagic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vmcomms.ptemagic.service.AnswerService;
import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.web.rest.errors.BadRequestAlertException;
import com.vmcomms.ptemagic.web.rest.util.HeaderUtil;
import com.vmcomms.ptemagic.web.rest.util.PaginationUtil;
import com.vmcomms.ptemagic.service.dto.AnswerDTO;
import io.swagger.annotations.ApiParam;
import liquibase.util.StringUtils;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Answer.
 */
@RestController
@RequestMapping("/api")
public class AnswerResource {

    private final Logger log = LoggerFactory.getLogger(AnswerResource.class);

    private static final String ENTITY_NAME = "answer";

    @Autowired
    private AnswerService answerService;
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private Environment env;

    /**
     * POST  /answers : Create a new answer.
     *
     * @param answerDTO the answerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new answerDTO, or with status 400 (Bad Request) if the answer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/answers")
    @Timed
    public ResponseEntity<AnswerDTO> createAnswer(@RequestBody AnswerDTO answerDTO) throws URISyntaxException {
        log.debug("REST request to save Answer : {}", answerDTO);
        if (answerDTO.getId() != null) {
            throw new BadRequestAlertException("A new answer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        // Update audio link
        if (StringUtils.isNotEmpty(answerDTO.getAudioLink())) {
        	String newAudioLink = "https://storage.googleapis.com/" + env.getProperty("google-cloud.storage.bucket-name-answer") + "/" + answerDTO.getAudioLink();
            answerDTO.setAudioLink(newAudioLink);
        }
        
        AnswerDTO result = answerService.save(answerDTO);
        return ResponseEntity.created(new URI("/api/answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /answers : Updates an existing answer.
     *
     * @param answerDTO the answerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated answerDTO,
     * or with status 400 (Bad Request) if the answerDTO is not valid,
     * or with status 500 (Internal Server Error) if the answerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/answers")
    @Timed
    public ResponseEntity<AnswerDTO> updateAnswer(@RequestBody AnswerDTO answerDTO) throws URISyntaxException {
        log.debug("REST request to update Answer : {}", answerDTO);
        if (answerDTO.getId() == null) {
            return createAnswer(answerDTO);
        }
        AnswerDTO result = answerService.save(answerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, answerDTO.getId().toString()))
            .body(result);
    }

    
    @PutMapping("/answer-update-status")
    @Timed
    public ResponseEntity<AnswerDTO> updateStatusAnswer(@RequestBody AnswerDTO answerDTO) throws URISyntaxException {
        log.debug("REST request to update Answer : {}", answerDTO);

        AnswerDTO result = answerService.findOne(answerDTO.getId());
        result.setScore(answerDTO.getScore());
        result.setStatus("DONE");
        
        // Save
        result = answerService.save(result);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, answerDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * GET  /answers : get all the answers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of answers in body
     */
    @GetMapping("/answers")
    @Timed
    public ResponseEntity<List<AnswerDTO>> getAllAnswers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Answers");
        Page<AnswerDTO> page = answerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/answers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /answers/:id : get the "id" answer.
     *
     * @param id the id of the answerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the answerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/answers/{id}")
    @Timed
    public ResponseEntity<AnswerDTO> getAnswer(@PathVariable Long id) {
        log.debug("REST request to get Answer : {}", id);
        AnswerDTO answerDTO = answerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(answerDTO));
    }

    /**
     * DELETE  /answers/:id : delete the "id" answer.
     *
     * @param id the id of the answerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/answers/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        log.debug("REST request to delete Answer : {}", id);
        answerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
