package com.organizadorcarrera.util;

import com.organizadorcarrera.builder.StageBuilder;
import com.organizadorcarrera.controller.CourseController;
import com.organizadorcarrera.controller.MainController;
import com.organizadorcarrera.model.Course;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WindowDirector {

    private final StageBuilder stageBuilder;

    public void showMainWindow(Stage stage) {
        stageBuilder
                .initFrom(stage)
                .setFXMLScene("/fxml/Main.fxml")
                .withTitle("TituloVentanaPrincipal")
                .doOnCloseRequest(((MainController) stageBuilder.getLoader().getController())::closeWindow)
                .build()
                .show();
    }

    public void showNewCourseWindow() {
        stageBuilder
                .init()
                .setFXMLScene("/fxml/Course.fxml")
                .withTitle("TituloVentanaAgregar")
                .asApplicationModal()
                .build()
                .showAndWait();
    }

    public void showEditCourseWindow(Course materia) {
        stageBuilder
                .init()
                .setFXMLScene("/fxml/Course.fxml")
                .withTitle("TituloVentanaEditar")
                .asApplicationModal()
                .doWithController((CourseController controller) -> controller.injectCourse(materia))
                .build()
                .showAndWait();
    }

    public void showExitWindow() {
        stageBuilder
                .init()
                .setFXMLScene("/fxml/Exit.fxml")
                .withTitle("TituloVentanaConfirmarCierre")
                .asApplicationModal()
                .build()
                .showAndWait();
    }

}
