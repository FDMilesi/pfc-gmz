package ar.edu.utn.frsf.kinesio.test.util;

import java.util.HashMap;
import java.util.Map;
import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class ContextMocker extends FacesContext {

    private ContextMocker() {
    }

    private static final Release RELEASE = new Release();
    
    public static int messageCount = 0;

    private static class Release implements Answer<Void> {

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            setCurrentInstance(null);
            return null;
        }
    }

    public static FacesContext mockFacesContext() {
        FacesContext context = Mockito.mock(FacesContext.class);
        Map<String, Object> sessionMap = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
//        HttpServletRequest request = mock(HttpServletRequest.class);
        ExternalContext ext = mock(ExternalContext.class);
//        when(ext.getRequest()).thenReturn(request);
//        when(ext.isUserInRole(anyString())).thenReturn(true);
        when(ext.getSessionMap()).thenReturn(sessionMap);
        when(ext.getRequestParameterMap()).thenReturn(requestMap);
        when(context.getExternalContext()).thenReturn(ext);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ContextMocker.messageCount++;
                return null;
            }
        }).when(context).addMessage(anyString(), (FacesMessage) anyObject());
        
        Application application = mock(Application.class);
        when(context.getApplication()).thenReturn(application);        
        
        ELContext eLContext = mock (ELContext.class);
        when(context.getELContext()).thenReturn(eLContext);
        
        setCurrentInstance(context);
        
        Mockito.doAnswer(RELEASE)
                .when(context)
                .release();
        return context;
    }
}
