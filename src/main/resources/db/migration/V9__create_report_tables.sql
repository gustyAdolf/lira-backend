CREATE TABLE IF NOT EXISTS report_type
(
    id          SMALLINT    NOT NULL,
    report_type VARCHAR(80) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE report_type OWNER TO lira;

INSERT INTO report_type (id, report_type) VALUES
    (1, 'Informe de derivación'),
    (2, 'Justificante asistencia'),
    (3, 'Comunicación clinica'),
    (4, 'Certificado de tratamiento')
ON CONFLICT (id) DO NOTHING;

CREATE TABLE IF NOT EXISTS reports
(
    id             SMALLINT NOT NULL,
    therapist_id   INTEGER  NOT NULL,
    user_id        INTEGER  NOT NULL,
    report_type_id INTEGER  NOT NULL,
    data_report    TEXT     NOT NULL,
    created_at     DATE     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (therapist_id) REFERENCES users ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE,
    FOREIGN KEY (report_type_id) REFERENCES report_type ON DELETE CASCADE
);

ALTER TABLE reports OWNER TO lira;
