#include "window_singlethreaded.h"
#include "cuberenderer.h"
#include <QOpenGLContext>
#include <QOpenGLFunctions>
#include <QOpenGLFramebufferObject>
#include <QOpenGLShaderProgram>
#include <QOpenGLVertexArrayObject>
#include <QOpenGLBuffer>
#include <QOpenGLVertexArrayObject>
#include <QOffscreenSurface>
#include <QScreen>
#include <QQmlEngine>
#include <QQmlComponent>
#include <QQuickItem>
#include <QQuickWindow>
#include <QQuickRenderControl>
#include <QCoreApplication>



#include <QList>

#include <QObject>
#include <QVariant>
#include <QQuickItem>



#include <QQmlContext>

#include <QDateTime>








QObject *score,*distance,*particleCoin,*startCoin;

class RenderControl : public QQuickRenderControl
{
public:
    RenderControl(QWindow *w) : m_window(w) { }
    QWindow *renderWindow(QPoint *offset) Q_DECL_OVERRIDE;

private:
    QWindow *m_window;
};

QWindow *RenderControl::renderWindow(QPoint *offset)
{
    if (offset)
        *offset = QPoint(0, 0);
    return m_window;
}

WindowSingleThreaded::WindowSingleThreaded()
    : m_rootItem(0),
      m_fbo(0),
      m_quickInitialized(false),
      m_quickReady(false),
      m_dpr(0)
{
    setSurfaceType(QSurface::OpenGLSurface);

    QSurfaceFormat format;
    // Qt Quick may need a depth and stencil buffer. Always make sure these are available.
    format.setDepthBufferSize(16);
    format.setStencilBufferSize(8);
    format.setAlphaBufferSize(8);
    format.setBlueBufferSize(8);
    format.setGreenBufferSize(8);
    format.setRedBufferSize(8);


    setFormat(format);

    m_context = new QOpenGLContext;
    m_context->setFormat(format);
    m_context->create();

    m_offscreenSurface = new QOffscreenSurface;
    // Pass m_context->format(), not format. Format does not specify and color buffer
    // sizes, while the context, that has just been created, reports a format that has
    // these values filled in. Pass this to the offscreen surface to make sure it will be
    // compatible with the context's configuration.
    m_offscreenSurface->setFormat(m_context->format());

    m_offscreenSurface->create();



    m_cubeRenderer = new CubeRenderer(m_offscreenSurface);

    m_renderControl = new RenderControl(this);

    // Create a QQuickWindow that is associated with out render control. Note that this
    // window never gets created or shown, meaning that it will never get an underlying
    // native (platform) window.
    m_quickWindow = new QQuickWindow(m_renderControl);

    m_quickWindow->setFormat(m_context->format());


    // Create a QML engine.
    m_qmlEngine = new QQmlEngine;
    if (!m_qmlEngine->incubationController())
        m_qmlEngine->setIncubationController(m_quickWindow->incubationController());








    // When Quick says there is a need to render, we will not render immediately. Instead,
    // a timer with a small interval is used to get better performance.
    m_updateTimer.setSingleShot(true);
    m_updateTimer.setInterval(0);
    connect(&m_updateTimer, &QTimer::timeout, this, &WindowSingleThreaded::render);

    // Now hook up the signals. For simplicy we don't differentiate between
    // renderRequested (only render is needed, no sync) and sceneChanged (polish and sync
    // is needed too).
    connect(m_quickWindow, &QQuickWindow::sceneGraphInitialized, this, &WindowSingleThreaded::createFbo);
    connect(m_quickWindow, &QQuickWindow::sceneGraphInvalidated, this, &WindowSingleThreaded::destroyFbo);
    connect(m_renderControl, &QQuickRenderControl::renderRequested, this, &WindowSingleThreaded::requestUpdate);
    connect(m_renderControl, &QQuickRenderControl::sceneChanged, this, &WindowSingleThreaded::requestUpdate);




    // Just recreating the FBO on resize is not sufficient, when moving between screens
    // with different devicePixelRatio the QWindow size may remain the same but the FBO
    // dimension is to change regardless.
    connect(this, &QWindow::screenChanged, this, &WindowSingleThreaded::handleScreenChange);
}

WindowSingleThreaded::~WindowSingleThreaded()
{
    // Make sure the context is current while doing cleanup. Note that we use the
    // offscreen surface here because passing 'this' at this point is not safe: the
    // underlying platform window may already be destroyed. To avoid all the trouble, use
    // another surface that is valid for sure.
    m_context->makeCurrent(m_offscreenSurface);

    // Delete the render control first since it will free the scenegraph resources.
    // Destroy the QQuickWindow only afterwards.
    delete m_renderControl;

    delete m_qmlComponent;
    delete m_quickWindow;
    delete m_qmlEngine;
    delete m_fbo;

    m_context->doneCurrent();

    delete m_cubeRenderer;

    delete m_offscreenSurface;
    delete m_context;
}

void WindowSingleThreaded::createFbo()
{
    // The scene graph has been initialized. It is now time to create an FBO and associate
    // it with the QQuickWindow.
    m_dpr = devicePixelRatio();
    m_fbo = new QOpenGLFramebufferObject(size() * m_dpr, QOpenGLFramebufferObject::CombinedDepthStencil);
    m_quickWindow->setRenderTarget(m_fbo);
}

void WindowSingleThreaded::destroyFbo()
{
    delete m_fbo;
    m_fbo = 0;
}

void WindowSingleThreaded::render()
{
    if (!m_context->makeCurrent(m_offscreenSurface))
        return;

    // Polish, synchronize and render the next frame (into our fbo).  In this example
    // everything happens on the same thread and therefore all three steps are performed
    // in succession from here. In a threaded setup the render() call would happen on a
    // separate thread.
    m_renderControl->polishItems();
    m_renderControl->sync();
    m_renderControl->render();

    m_quickWindow->resetOpenGLState();
    QOpenGLFramebufferObject::bindDefault();

    m_context->functions()->glFlush();

    m_quickReady = true;

    // Get something onto the screen.
    m_cubeRenderer->render(this, m_context, m_quickReady ? m_fbo->texture() : 0);

    int myInt = m_cubeRenderer->score();
    QString text = "Score: " + QString::number( myInt )+" points";
    if(m_cubeRenderer->flagGPS()==1)
    score->setProperty("text",text);
    else
        score->setProperty("text","enable GPS settings");


    QString textD = "Distance: " + QString::number( m_cubeRenderer->distance() )+" m. Total Coins: "+QString::number( m_cubeRenderer->totalCoins() );
    distance->setProperty("text",textD);

    int *posCoinSelected=m_cubeRenderer->posCoin();
    particleCoin->setProperty("posxC",posCoinSelected[0]);
    particleCoin->setProperty("posyC",posCoinSelected[1]);


    startCoin->setProperty("corre",m_cubeRenderer->startCoin() );
    score->setProperty("corre",m_cubeRenderer->startCoin() );

    if(startCoin->property("control").value<int>()>1)
    {
    m_cubeRenderer->stopCoin();
    startCoin->setProperty("control",0 );
    }


}

void WindowSingleThreaded::requestUpdate()
{
    if (!m_updateTimer.isActive())
        m_updateTimer.start();
}

void WindowSingleThreaded::run()
{


    if (m_qmlComponent->isError()) {
        QList<QQmlError> errorList = m_qmlComponent->errors();
        foreach (const QQmlError &error, errorList)
            qWarning() << error.url() << error.line() << error;
        return;
    }

    QObject *rootObject = m_qmlComponent->create();
    if (m_qmlComponent->isError()) {
        QList<QQmlError> errorList = m_qmlComponent->errors();
        foreach (const QQmlError &error, errorList)
            qWarning() << error.url() << error.line() << error;
        return;
    }




    m_rootItem = qobject_cast<QQuickItem *>(rootObject);
    if (!m_rootItem) {
        qWarning("run: Not a QQuickItem");
        delete rootObject;
        return;
    }

    // The root item is ready. Associate it with the window.
    m_rootItem->setParentItem(m_quickWindow->contentItem());



    score = m_rootItem->findChild<QObject*>("objectText");
    distance = m_rootItem->findChild<QObject*>("objectDistance");
     particleCoin = m_rootItem->findChild<QObject*>("particleCoin");
     startCoin=m_rootItem->findChild<QObject*>("startCoin");





  QList<QQuickItem*> l=  m_rootItem->childItems();



foreach ( QQuickItem *error, l)
    qWarning() <<   error;





    // Update item and rendering related geometries.
    updateSizes();

    // Initialize the render control and our OpenGL resources.
    m_context->makeCurrent(m_offscreenSurface);
    m_renderControl->initialize(m_context);
    m_quickInitialized = true;
}

void WindowSingleThreaded::updateSizes()
{
    // Behave like SizeRootObjectToView.
    m_rootItem->setWidth(width());
    m_rootItem->setHeight(height());

    m_quickWindow->setGeometry(0, 0, width(), height());

    m_cubeRenderer->resize(width(), height());
}

void WindowSingleThreaded::startQuick(const QString &filename)
{


    if (m_qmlComponent->isLoading())
        connect(m_qmlComponent, &QQmlComponent::statusChanged, this, &WindowSingleThreaded::run);
    else
        run();
}

void WindowSingleThreaded::exposeEvent(QExposeEvent *)
{
    if (isExposed()) {
        if (!m_quickInitialized) {
            m_cubeRenderer->render(this, m_context, m_quickReady ? m_fbo->texture() : 0);


            startQuick(QStringLiteral("qrc:/rendercontrol/main.qml"));
        }
    }
}

void WindowSingleThreaded::resizeFbo()
{
    if (m_rootItem && m_context->makeCurrent(m_offscreenSurface)) {
        delete m_fbo;
        createFbo();
        m_context->doneCurrent();
        updateSizes();
        render();
    }
}

void WindowSingleThreaded::resizeEvent(QResizeEvent *)
{
    // If this is a resize after the scene is up and running, recreate the fbo and the
    // Quick item and scene.
    if (m_fbo && m_fbo->size() != size() * devicePixelRatio())
        resizeFbo();
}

void WindowSingleThreaded::handleScreenChange()
{
    if (m_dpr != devicePixelRatio())
        resizeFbo();
}

void WindowSingleThreaded::mousePressEvent(QMouseEvent *e)
{
    // Use the constructor taking localPos and screenPos. That puts localPos into the
    // event's localPos and windowPos, and screenPos into the event's screenPos. This way
    // the windowPos in e is ignored and is replaced by localPos. This is necessary
    // because QQuickWindow thinks of itself as a top-level window always.
    QMouseEvent mappedEvent(e->type(), e->localPos(), e->screenPos(), e->button(), e->buttons(), e->modifiers());



    QCoreApplication::sendEvent(m_quickWindow, &mappedEvent);

}

void WindowSingleThreaded::mouseReleaseEvent(QMouseEvent *e)
{
    QMouseEvent mappedEvent(e->type(), e->localPos(), e->screenPos(), e->button(), e->buttons(), e->modifiers());
    QCoreApplication::sendEvent(m_quickWindow, &mappedEvent);




}


