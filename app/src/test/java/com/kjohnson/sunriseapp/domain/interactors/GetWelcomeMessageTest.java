package com.kjohnson.sunriseapp.domain.interactors;


import com.kjohnson.sunriseapp.domain.executor.Executor;
import com.kjohnson.sunriseapp.domain.executor.MainThread;
import com.kjohnson.sunriseapp.domain.interactors.impl.WelcomingInteractorImpl;
import com.kjohnson.sunriseapp.domain.repository.MessageRepository;
import com.kjohnson.sunriseapp.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Tests our welcoming interactor.
 */
public class GetWelcomeMessageTest {

    private       MainThread                   mMainThread;
    @Mock private Executor                     mExecutor;
    @Mock private MessageRepository            mMessageRepository;
    @Mock private WelcomingInteractor.Callback mMockedCallback;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
    }

    @Test
    public void testWelcomeMessageNotFound() throws Exception {
        WelcomingInteractorImpl interactor = new WelcomingInteractorImpl(mExecutor, mMainThread, mMockedCallback, mMessageRepository);
        interactor.run();

        Mockito.when(mMessageRepository.getWelcomeMessage())
                .thenReturn(null);

        Mockito.verify(mMessageRepository).getWelcomeMessage();
        Mockito.verifyNoMoreInteractions(mMessageRepository);
        Mockito.verify(mMockedCallback).onRetrievalFailed(anyString());
    }

    @Test
    public void testWelcomeMessageFound() throws Exception {

        String msg = "Welcome, friend!";

        when(mMessageRepository.getWelcomeMessage())
                .thenReturn(msg);

        WelcomingInteractorImpl interactor = new WelcomingInteractorImpl(mExecutor, mMainThread, mMockedCallback, mMessageRepository);
        interactor.run();

        Mockito.verify(mMessageRepository).getWelcomeMessage();
        Mockito.verifyNoMoreInteractions(mMessageRepository);
        Mockito.verify(mMockedCallback).onMessageRetrieved(msg);
    }
}